package com.cloud9ers.play2.memcached


import play.api.Plugin
import play.api.Application
import net.spy.memcached.{ ConnectionFactoryBuilder, AddrUtil, MemcachedClient }
import net.spy.memcached.auth.AuthDescriptor
import net.spy.memcached.auth.PlainCallbackHandler
import play.api.Logger
import net.spy.memcached.transcoders.Transcoder
import java.util.concurrent.TimeUnit
import play.api.mvc.Request
import play.api.Play

class MemcachedPlugin(app: Application) extends Plugin {

  lazy val logger = Logger("memcached.plugin")
  
  override def onStart() {
    SessionsCache.instance
    logger.info("Starting MemcachedPlugin.")
  }

  override def onStop() {
    logger.info("Stopping MemcachedPlugin.")
    SessionsCache.instance.shutdown
    Thread.interrupted()
  }

}
object MemcachedPlugin{
   implicit def globalSesssion[A](request: Request[A]) = new GlobalSession[A](request)
   class GlobalSession[A](request: Request[A]) {
  def globalSession = {
    SessionsCache.instance.get(request.cookies.get("_foundation_session").get.value)
  }
   }
}
class SessionsCache(app: Application) {

  lazy val logger = Logger("memcached.plugin.MemcachedSessionClient")

  lazy val client = {

    lazy val singleHost = app.configuration.getString("memcached.host").map(AddrUtil.getAddresses)
    lazy val multipleHosts = app.configuration.getString("memcached.1.host").map { _ =>
      def accumulate(nb: Int): String = {
        app.configuration.getString("memcached." + nb + ".host").map { h => h + " " + accumulate(nb + 1) }.getOrElse("")
      }
      AddrUtil.getAddresses(accumulate(1))
    }

    val addrs = singleHost.orElse(multipleHosts)
      .getOrElse(throw new RuntimeException("Bad configuration for memcached: missing host(s)"))

    app.configuration.getString("memcached.user").map { memcacheUser =>
      val memcachePassword = app.configuration.getString("memcached.password").getOrElse {
        throw new RuntimeException("Bad configuration for memcached: missing password")
      }
      // Use plain SASL to connect to memcached
      val ad = new AuthDescriptor(Array("PLAIN"),
        new PlainCallbackHandler(memcacheUser, memcachePassword))
      val cf = new ConnectionFactoryBuilder()
        .setProtocol(ConnectionFactoryBuilder.Protocol.BINARY)
        .setAuthDescriptor(ad)
        .build()
      new MemcachedClient(cf, addrs)
    }.getOrElse {
      new MemcachedClient(addrs)
    }
  }

  lazy val namespace: String = app.configuration.getString("memcached.namespace").getOrElse("sessions:")

  def get(key: String) = {
    logger.debug("Getting the cached for key " + namespace + key)
    val future = client.asyncGet(namespace + key)
    try {
      val any = future.get(1, TimeUnit.SECONDS)
      if (any != null) {
        logger.debug("any is " + any.getClass)
      }
      Option(
        any match {
          case x: java.lang.Byte => x.byteValue()
          case x: java.lang.Short => x.shortValue()
          case x: java.lang.Integer => x.intValue()
          case x: java.lang.Long => x.longValue()
          case x: java.lang.Float => x.floatValue()
          case x: java.lang.Double => x.doubleValue()
          case x: java.lang.Character => x.charValue()
          case x: java.lang.Boolean => x.booleanValue()
          case x => x
        })
    } catch {
      case e: Throwable =>
        logger.error("An error has occured while getting the value from memcached", e)
        future.cancel(false)
        None
    }
  }

  def shutdown {
    client.shutdown()
  }

}

object SessionsCache {
  private lazy val INSTANCE: SessionsCache = new SessionsCache(Play.current)
  def instance = INSTANCE
}