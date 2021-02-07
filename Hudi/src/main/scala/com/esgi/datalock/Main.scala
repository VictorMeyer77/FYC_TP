package com.esgi.datalock

import com.esgi.datalock.delta.{Ranking, Storer, TablesCreate}
import com.esgi.datalock.utils.Checkpoint
import org.apache.spark.sql.SparkSession

import org.apache.hudi.QuickstartUtils._
import scala.collection.JavaConversions._
import org.apache.spark.sql.SaveMode._
import org.apache.hudi.DataSourceReadOptions._
import org.apache.hudi.DataSourceWriteOptions._
import org.apache.hudi.config.HoodieWriteConfig._

object Main {

  def main(args: Array[String]): Unit ={

    val spark: SparkSession = SparkSession
      .builder()
      .appName("Artist Tracking Hudi")
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .getOrCreate()

    //TablesCreate.run(spark)

    if(args.length < 1){

      println("ERROR: Prend en argument 'store' ou 'rank'.")

    }
    else if(args(0) == "store"){

      if(args.length < 2){

        println("ERROR: Store prend un argument: le chemin du dossier des données brutes.")

      }
      else{

        val storer: Storer = new Storer(spark)

        val artistFilesToProcess: List[String] = Checkpoint.getListFilesToProcess("artist", args(1) + "/artist")
        val trackFilesToProcess: List[String] = Checkpoint.getListFilesToProcess("tracklist", args(1) + "/tracklist")

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
    else if(args(0) == "rank"){

      val ranking: Ranking = new Ranking(spark)
      ranking.run()

    }
  }
}
