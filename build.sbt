name := "anser"

version := "1.0"

scalaVersion := "2.11.5"

scalacOptions := Seq("-unchecked", "-deprecation", "-feature", "-encoding", "utf8")


libraryDependencies ++= {
  val akkaV = "2.3.9"
  val akkaStreamV = "1.0-M2"
  val scalaTestV = "2.2.1"
  Seq(
    "com.typesafe.akka" %% "akka-actor"                        % akkaV,
    "com.typesafe.akka" %% "akka-stream-experimental"          % akkaStreamV,
    "com.typesafe.akka" %% "akka-http-core-experimental"       % akkaStreamV,
    "com.typesafe.akka" %% "akka-http-experimental"            % akkaStreamV,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaStreamV,
    "com.typesafe.akka" %% "akka-http-testkit-experimental"    % akkaStreamV,
    "org.scalatest"     %% "scalatest"                         % scalaTestV % "test",
    "com.typesafe.akka" %% "akka-testkit"                      % scalaTestV % "test"
  )
}

Revolver.settings