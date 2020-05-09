import com.google.inject.AbstractModule
import com.quirl.actors.SupervisorActor
import play.api.Logging
import play.api.libs.concurrent.AkkaGuiceSupport

class Module extends AbstractModule with AkkaGuiceSupport with Logging {
  logger.info("Module::Module")

  override def configure(): Unit = {
    bindActor[SupervisorActor]("SupervisorActor")
  }
}
