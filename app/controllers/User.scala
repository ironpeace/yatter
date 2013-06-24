package controllers

import play.api._
import play.api.mvc._
import models._
import com.github.aselab.activerecord.dsl._

object Users extends Controller {
  
  def list = Action { implicit request =>
	Ok(views.html.user_list(User.all.toList))
  }
}