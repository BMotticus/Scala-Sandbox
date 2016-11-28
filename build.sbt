
name := """bm-scalaSandBox"""

organization := "brandonmott"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.typelevel"     %% "cats" % "0.6.1",
  "org.scalaz"        %% "scalaz-core" % "7.1.3",
  "com.typesafe.akka" %% "akka-actor" % "2.4.12",
  "com.typesafe.akka" %% "akka-agent" % "2.4.12",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.12",
  "org.scalatest" % "scalatest_2.11" % "3.0.0" % "test",
//  "org.scalamock"     %% "scalamock-scalatest-support" % "3.2" % "test",
//  "org.mockito"       % "mockito-core" % "1.9.5",
  "commons-io"        % "commons-io" % "2.5",
  "com.squants"       %% "squants" % "0.6.2",
  "org.threeten"      % "threeten-extra" % "1.0",
  "joda-time"         % "joda-time" % "2.7"
)

offline := true

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
