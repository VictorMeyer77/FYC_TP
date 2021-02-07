package com.esgi.datalock

import com.esgi.datalock.delta.Storer
import com.esgi.datalock.utils.Checkpoint
import org.apache.spark.sql.SparkSession

object Main {

  def main(args: Array[String]): Unit ={

    val spark: SparkSession = SparkSession
      .builder()
      .appName("Artist Tracking Hudi")
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .getOrCreate()

    if(args.length < 1){

      println("ERROR: Prend un argument: le chemin du dossier des données brutes.")

    }
    else{

      val storer: Storer = new Storer(spark)

      val artistFilesToProcess: List[String] = Checkpoint.getListFilesToProcess("artist", args(0) + "/artist")
      val trackFilesToProcess: List[String] = Checkpoint.getListFilesToProcess("tracklist", args(0) + "/tracklist")

      for(file <- artistFilesToProcess){
        storer.store(file, "artist")
      }

      for(file <- trackFilesToProcess){
        storer.store(file, "tracklist")
      }

      Checkpoint.updateCheckpoint("artist", artistFilesToProcess)
      Checkpoint.updateCheckpoint("tracklist", trackFilesToProcess)

    }
  }
}
