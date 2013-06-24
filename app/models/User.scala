package models

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._

import java.security.MessageDigest

case class User(
	@Required(on="create")
	name: String, 

	@Transient 
	@Required
	password: String

	) extends ActiveRecord {

		var hashedPassword:String = _
		override def beforeSave() {
			if (password != null && password != "")
				hashedPassword = User.hash(password)
		}

		lazy val follows = hasAndBelongsToMany[Follow]

		override def afterSave() {
			Follow(this.id).create
		}

		lazy val tweets = hasMany[Tweet]
}

object User extends ActiveRecordCompanion[User]{

	def hash(target : String):String = {
		MessageDigest
			.getInstance("SHA1")
			.digest(target.getBytes)
			.map(_ & 0xFF)
			.map(_.toHexString)
			.mkString
	}
}
