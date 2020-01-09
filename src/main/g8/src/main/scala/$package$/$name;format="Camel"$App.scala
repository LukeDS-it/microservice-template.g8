package $package$

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.Materializer
import com.typesafe.config.ConfigFactory
import $package$.config.AppConfig
import $package$.http.BasicRoutes

import scala.concurrent.ExecutionContext
import scala.util.Success

object $name;format="Camel"$App extends App with BasicRoutes {
  
  logger.info("Starting $name$")
  
  lazy val appConfig = AppConfig(ConfigFactory.load())
  lazy val serverPort = appConfig.serverPort
  
  implicit lazy val system: ActorSystem = ActorSystem("$name;format="lower,hyphen"$-system")
  implicit lazy val materializer: Materializer = Materializer(system)
  implicit lazy val ec: ExecutionContext = system.dispatcher
  
  val http = Http(system)
  
  http.bindAndHandle(routes, "0.0.0.0", serverPort).onComplete {
    case Success(_) => logger.info(s"$name$ listening on http port \$serverPort")
    case _ => logger.error(s"Couldn't bind on port \$serverPort");
  }
  
  sys.addShutdownHook {
    system.terminate()
  }
  
}
