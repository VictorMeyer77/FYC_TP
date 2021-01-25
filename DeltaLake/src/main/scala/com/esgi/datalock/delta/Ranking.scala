package com.esgi.datalock.delta

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._

class Ranking(spark: SparkSession) {

  def run(): Unit ={

    val stream: DataFrame = spark
      .readStream
      .format("delta")
      .load("warehouse/delta/music_tracking")


    stream.filter("date_maj == " + current_date())
      .filter("popularity >= 75")
      .groupBy("artist_id")
      .count()
      .withColumnRenamed("count", "nb_top_track")
      .writeStream
      .format("delta")
      .outputMode("complete")
      .option("checkpointLocation", "checkpoints/top_track_by_artist")
      .start("warehouse/delta/top_track_by_artist")
      .awaitTermination()

  }
}
