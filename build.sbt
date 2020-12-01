name := "fall20ex10_starter"

version := "0.1"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  guice,
  javaJdbc,
  javaJpa,
  evolutions,
  "com.adrianhurt" %% "play-bootstrap" % "1.6.1-P28-B4",
  "org.hibernate" % "hibernate-core" % "5.4.9.Final",
  "com.h2database" % "h2" % "1.4.192"
)
