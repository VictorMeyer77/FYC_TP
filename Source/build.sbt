name := "Source"

version := "0.1"

scalaVersion := "2.12.2"

libraryDependencies +=  "org.scalaj" %% "scalaj-http" % "2.4.2"
libraryDependencies += "com.google.code.gson" % "gson" % "1.7.1"


mainClass in (Compile, run) := Some("com.esgi.datalock.Main")