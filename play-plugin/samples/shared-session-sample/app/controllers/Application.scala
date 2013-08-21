package controllers

import play.api._
import play.api.mvc._
import com.cloud9ers.play2.memcached.MemcachedPlugin._
import play.api.libs.json.JsObject
import play.api.libs.json.JsObject
import play.api.libs.json.JsObject
object Application extends Controller {
  
  def index = Action { request =>
    request.globalSession match {
      case Some(session: JsObject) =>
        Ok(session)
      case None =>
        Ok("None")
      case _ =>
        Ok("invalid session")
    }
  }
  
}