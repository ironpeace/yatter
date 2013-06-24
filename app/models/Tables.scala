package models

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._

import com.typesafe.config._
import play.api.Play.current

object Tables extends ActiveRecordTables {
  val users = table[User]
  val follows = table[Follow]
  val tweets = table[Tweet]
}