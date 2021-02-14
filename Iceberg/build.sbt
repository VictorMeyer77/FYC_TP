name := "Iceberg"

version := "0.1"

scalaVersion := "2.12.2"

sbtPlugin := true

libraryDependencies += "com.google.code.gson" % "gson" % "1.7.1"
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.5" % "compile"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.5" % "compile"
libraryDependencies += "org.apache.iceberg" % "iceberg-spark3-runtime" % "0.11.0"

assemblyMergeStrategy in assembly := {
  case "META-INF/services/org.apache.spark.sql.sources.DataSourceRegister" => MergeStrategy.concat
  case PathList("META-INF", xs @ _*) =>
    xs map {_.toLowerCase} match {
      case ("manifest.mf" :: Nil) | ("index.list" :: Nil) | ("dependencies" :: Nil) =>
        MergeStrategy.discard
      case "services" :: _ =>  MergeStrategy.filterDistinctLines
      case ps @ (x :: xs) if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") =>
        MergeStrategy.discard
      case _ => MergeStrategy.first
    }
  case _ => MergeStrategy.first
}

mainClass in (Compile, run) := Some("com.esgi.datalock.Main")
mainClass in assembly := Some("com.esgi.datalock.Main")