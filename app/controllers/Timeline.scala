package controllers

import models._
import views._

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import com.github.aselab.activerecord.dsl._


object Timeline extends Controller {

	def index = Action { implicit request =>
		session.get("username").map { username =>
			User.findBy("name", username) match {
				case Some(user) => {

					// The implementation to show the timeline view

					// ******************************************
					// Get users who the logined user follows.
					// After you specify the follow model, 
					// you can get the users easily.
					// ******************************************
					val whoUserFollows
					 = Follow.where(_.userid === user.id).head.users.toList

					// ******************************************
					// Get tweets of the logined user.
					// You can get the tweets easily.
					// ******************************************
					val myTweets = user.tweets.toList

					// ******************************************
					// Add follows' tweet to "myTweets"
					// ******************************************
					val tweets = whoUserFollows.foldLeft(myTweets)(
						(mine, w) => mine ::: w.tweets.toList
					)

					Ok(views.html.index(user)(whoUserFollows)(User.all.toList)(tweets))
				}
				case None => Ok(views.html.login(Application.loginForm)).withNewSession
			}
		}.getOrElse{
			Ok(views.html.login(Application.loginForm))	
		}
	}

	def follow(id:Long) = Action { implicit request =>
		session.get("username").map { username =>
			User.findBy("name", username) match {
				case Some(user) => {
					User.find(id) match {
						case Some(whoUserFollow) => {

							// The implementation to follow

							// ******************************************
							// Follow a user who the user wants to follow
							// (Add a user to the User's Follow Group)
							// ******************************************
							Follow.where(_.userid === user.id).head.users << whoUserFollow

							Redirect(routes.Timeline.index()).flashing{
								"success" -> ("You followed " + whoUserFollow.name)
							}
						}
						case None => InternalServerError("Who you want to follow disappeared!")
					}
				}
				case None => Ok(views.html.login(Application.loginForm)).withNewSession
			}
		}.getOrElse{
			Ok(views.html.login(Application.loginForm))	
		}
	}

	def post(text:String) = Action { implicit request =>
		session.get("username").map { username =>
			User.findBy("name", username) match {
				case Some(user) => {

					// The implementation to post a tweet

					// ******************************************
					// Create at tweet and Add to user models
					// ******************************************
					user.tweets << Tweet(user.id, text).create

					Redirect(routes.Timeline.index()).flashing{
						"success" -> ("Success to tweet")
					}
				}
				case None => Ok(views.html.login(Application.loginForm)).withNewSession
			}
		}.getOrElse{
			Ok(views.html.login(Application.loginForm))	
		}
	}

}