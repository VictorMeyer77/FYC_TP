name := "Hudi"

version := "0.1"

scalaVersion := "2.11.0"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.7" % "compile"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.7" % "compile"
libraryDependencies += "org.apache.hudi" % "hudi-spark" % "0.5.0-incubating"

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