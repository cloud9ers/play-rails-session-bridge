package controllers

import play.api._
import play.api.mvc._
import com.cloud9ers.play2.memcached.MemcachedPlugin._
object Application extends Controller {
  
  def index = Action { request =>
    println(request)
    println(request.globalSession)
    Ok(views.html.index(request.cookies.get("_foundation_session").get.value))
  }
  
}