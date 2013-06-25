package controllers

import models._
import views._

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import com.github.aselab.activerecord.dsl._


object Application extends Controller {

	// ******************************************
	// Defining form helper is messy.
	// Scala-ActiveRecord will be able to omit it.
	// ******************************************
	val loginForm = Form(
		mapping(
		"name" -> text,
		"password" -> text
		)(User.apply)(User.unapply)
	)

	def index = Action { implicit request =>
		session.get("username").map { username =>
			User.findBy("name", username) match {
				case Some(user) => Redirect(routes.Timeline.index()).withSession("username" -> user.name)
				case None => Ok(views.html.login(loginForm)).withNewSession
			}
		}.getOrElse{
			Ok(views.html.login(loginForm))	
		}
	}

 	def login = Action { implicit request =>
	  	session.get("username").map { username =>
			User.findBy("name", username) match {
				case Some(user) => Redirect(routes.Timeline.index()).withSession("username" -> user.name)
				case None => Ok(views.html.login(loginForm)).withNewSession
			}

		}.getOrElse{
			loginForm.bindFromRequest.fold(
		    	formWithErrors => Redirect(routes.Timeline.index()).flashing{ "error" -> "something is wrong" },
		    	user => {
					// ******************************************
					// You can use the FindBy method.
					// However, the findBy method is not type-safe.
					// Scala-ActiveRecord recommends to use the where method 
					// instead of the findBy method.
					// ******************************************
				    User.where(_.name === user.name).headOption match {
						case Some(u) => {
							if(u.hashedPassword == User.hash(user.password)){
								Redirect(routes.Timeline.index())
									.withSession("username" -> user.name)
							}else{
								Redirect(routes.Application.login())
									.flashing{ "error" -> "Invalid name or password" }
							}
						}
						case None => Redirect(routes.Application.login())
							.flashing{ "error" -> "Invalid name or password" }
					}
		    	}
		    )
		}
	}

	def logout = Action { implicit request =>
	  	session.get("username").map { username =>
			Ok(views.html.login(loginForm)).withNewSession.flashing(
		    	"success" -> "You are now logged out."
		    )
		}.getOrElse{
			Ok(views.html.login(loginForm))	
		}
	}

	def signup = Action { implicit request =>
	  	session.get("username").map { username =>
	  		Redirect(routes.Timeline.index())
		}.getOrElse{
			loginForm.bindFromRequest.fold(
		    	formWithErrors => Redirect(routes.Timeline.index()).flashing{ "error" -> "something is wrong" },
		    	user => {
					// ******************************************
					// Here I use the exists method and the create method.
					// As you can see, you can write intuitively.
					// ******************************************
					if(User.exists(_.name === user.name)){
						Redirect(routes.Application.login())
							.flashing{ "error" -> "You're already signuped" }
					}else{
						val createduser = User(user.name, user.password).create
						Redirect(routes.Timeline.index())
							.withSession("username" -> createduser.name)
							.flashing{ "success" -> "Yatter!! Thank you for your signup!" }
					}
		    	}
		    )
		}
	}

}