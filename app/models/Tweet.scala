package models

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._
import java.sql.Timestamp

case class Tweet(
	@Required
	userid:Long,
	@Length(min=1, max=140)
	text:String
	) extends ActiveRecord {
		val createdAt:Timestamp
			 = new Timestamp(System.currentTimeMillis)

		val userId:Option[Long] = None
		lazy val user = belongsTo[User]
}

object Tweet extends ActiveRecordCompanion[Tweet]
