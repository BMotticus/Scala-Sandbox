
name := """sandbox"""

organization := "brandonmott"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.typelevel"     %% "cats" % "0.9.0",
  "org.scalaz"        %% "scalaz-core" % "7.2.10",
  "com.lihaoyi"       %% "ammonite" % "0.8.2" cross CrossVersion.full,
  "com.typesafe.akka" %% "akka-actor" % "2.5.0",
  "com.squants"       %% "squants" % "0.6.2",
  "com.lihaoyi"       %% "fastparse" % "0.4.2",
  "org.tpolecat"      %% "doobie-core" % "0.4.1",
  "commons-io"        % "commons-io" % "2.5",
  "org.threeten"      % "threeten-extra" % "1.0",
  "joda-time"         % "joda-time" % "2.7",
  "org.scalatest"     % "scalatest_2.11" % "3.0.0" % "test",
  "org.scalamock"     %% "scalamock-scalatest-support" % "3.2" % "test"
  //  "org.mockito"       % "mockito-core" % "1.9.5",
)

offline := true

initialCommands in console := """ammonite.Main().run()"""

//sources in doc in Compile := List()

resolvers ++= Seq(
  "typesafe" at "http://repo.typesafe.com/typesafe/releases/",
  Resolver.url("typesafe2", url("http://dl.bintray.com/typesafe/ivy-releases"))(Resolver.ivyStylePatterns),
  "nexus" at "https://oss.sonatype.org/content/repositories/snapshots"
)

//scalacOptions ++= Seq(
//  "-feature",
//  "-deprecation",
//  "-Xmax-classfile-name",
//  "-Xfatal-warnings",  
//  "200"
//)
