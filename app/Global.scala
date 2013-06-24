import play.api._
import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._
import models._

object Global extends GlobalSettings {
	override def onStart(app: Application) {
		Tables.initialize

		if(User.count == 0){
			val abe = User("shinzo_abe", "abe_pass").create
			val matz = User("yukihiro_matsumoto", "matz_pass").create
			val ichi = User("ichiro_suzuki", "ichiro_pass").create

			Follow.where(_.userid === matz.id).head.users << abe
			Follow.where(_.userid === matz.id).head.users << ichi

			abe.tweets << Tweet(abe.id, "I'm a prime minister.").create
			abe.tweets << Tweet(abe.id, "I'm a 96th prime minister.").create
			ichi.tweets << Tweet(ichi.id, "I think racking up the little feats is the only way to get someplace extraordinary.").create
			ichi.tweets << Tweet(ichi.id, "The satisfaction is in finding it.").create

		}
	}

	override def onStop(app: Application) {
		Tables.cleanup
	}
}