name := "bug-repro"

organization := "com.athlinks"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.8"

scalacOptions := Seq(
  "-Xfatal-warnings",
  "-Xlint:_",
  "-feature",
  "-language:implicitConversions",
  "-deprecation",
  "-unchecked"
)

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging"    %% "scala-logging"              % "3.1.0",
  "ch.qos.logback"                %  "logback-classic"            % "1.1.3",
  "com.typesafe"                  %  "config"                     % "1.3.0",
  "org.keycloak"                  %  "keycloak-admin-client"      % "1.9.8.Final",
  "org.jboss.resteasy"            %  "resteasy-client"            % "3.0.17.Final",
  "org.jboss.resteasy"            %  "resteasy-jackson2-provider" % "3.0.17.Final"
)

mainClass in (Compile, run) := Some("com.athlinks.Main")