
    sbt clean assembly
    
    spark-submit   --class com.esgi.datalock.Main target/scala-2.12/Hudi-assembly-0.1.jar  "../Source/output"
