
name := "bmott-scala-sandbox"

organization := "bmotticus"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.typelevel"     %% "cats" % "0.6.1",
  "com.typesafe.akka" %% "akka-actor" % "2.3.15",
  "org.scalaz"                %% "scalaz-core" % "7.1.3",
  "org.scalatest"    %% "scalatest" % "2.2.4" % "test",
  "org.scalamock"    %% "scalamock-scalatest-support" % "3.2" % "test",
  "org.mockito"      % "mockito-core" % "1.9.5",
  "commons-io"       % "commons-io" % "2.5",
  "org.threeten"     % "threeten-extra" % "1.0"//,
//  "mysql"  % "mysql-connector-java" % "5.1.36",
//  "com.typesafe.play" %% "play" % "2.4.6",
//  "com.typesafe.play" %% "play-jdbc" % "2.4.6",
//  "com.typesafe.play" %% "play-json" % "2.4.6",
//  "com.typesafe.play" %% "play-ws" % "2.4.6",
//  "io.prismic" %% "scala-kit" % "1.3.4"
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
