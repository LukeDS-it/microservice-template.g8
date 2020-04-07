val akkaVersion = "2.6.4"
val akkaHttpVersion = "10.1.11"
val akkaHttpCirceVersion = "1.31.0"
val pureconfigVersion = "0.12.3"
val scalaLoggingVersion = "3.9.2"
val logbackVersion = "1.2.3"
val logstashLogbackEncoderVersion = "5.2"
val scalatestVersion = "3.1.1"
val scalatestMockitoVersion = "1.0.0-M2"
val testcontainersVersion = "1.13.0"
val testcontainersScalaVersion = "0.36.1"
val circeVersion = "0.13.0"
val janinoVersion = "3.1.0"

val akkaDependencies = Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
)

val baseDependencies = Seq(
  "de.heikoseeberger" %% "akka-http-circe" % akkaHttpCirceVersion,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "com.github.pureconfig" %% "pureconfig" % pureconfigVersion,
  "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "org.codehaus.janino" % "janino" % janinoVersion,
  "net.logstash.logback" % "logstash-logback-encoder" % logstashLogbackEncoderVersion,
  "org.scalatest" %% "scalatest" % scalatestVersion % "test,it",
  "org.scalatestplus" %% "scalatestplus-mockito" % scalatestMockitoVersion,
  "org.testcontainers" % "testcontainers" % testcontainersVersion % "it",
  "com.dimafeng" %% "testcontainers-scala" % testcontainersScalaVersion % "it"
)

val customDependencies = Seq()

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .enablePlugins(JavaAppPackaging, DockerPlugin, AshScriptPlugin, DockerComposePlugin)
  .settings(Defaults.itSettings)
  .settings(CommonSettings.settings)
  .settings(DockerSettings.settings)
  .settings(ReleaseSettings.settings)
  .settings(
    organization := "$organization$",
    name := "$name;format="lower,hyphen"$",
    mainClass in Compile := Some("$package$.$name;format="Camel"$App"),
    scalaVersion := "2.13.1",
    fork in IntegrationTest := true,
    envVars in IntegrationTest := Map(
      "APP_VERSION" -> git.gitDescribedVersion.value.getOrElse((version in ThisBuild).value)
    ),
    libraryDependencies ++= akkaDependencies ++ baseDependencies ++ customDependencies
  )

addCommandAlias("integration", ";clean;compile;docker:publishLocal;it:test;")
