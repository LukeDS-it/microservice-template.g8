package $package$.testcontainers

import com.dimafeng.testcontainers.FixedHostPortGenericContainer
import com.typesafe.scalalogging.LazyLogging
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait

class TargetContainer(
  val dockerHost: String = "localhost",
  val appVersion: String = sys.env.getOrElse("APP_VERSION", "latest"),
  val targetPort: Int = $defaultServerPort$
) extends FixedHostPortGenericContainer(
      imageName = s"$dockerRepository$/$name;format="lower,hyphen"$:\$appVersion",
      waitStrategy = Some(Wait.forLogMessage(".*listening on http port.*\n", 1)),
      exposedHostPort = targetPort,
      exposedContainerPort = $defaultServerPort$,
    )
    with LazyLogging {

  def enableLogging(): Unit = {
    configure(c => c.followOutput(new Slf4jLogConsumer(logger.underlying)))
    ()
  }

}

