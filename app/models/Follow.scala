package models

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._


case class Follow(userid:Long) extends ActiveRecord {
	lazy val users = hasAndBelongsToMany[User]
}

object Follow extends ActiveRecordCompanion[Follow]