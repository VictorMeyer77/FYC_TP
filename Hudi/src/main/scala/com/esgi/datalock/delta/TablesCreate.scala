package com.esgi.datalock.delta

import org.apache.spark.sql.SparkSession

object TablesCreate {

  def run(spark: SparkSession): Unit ={

    spark.sql("CREATE TABLE IF NOT EXISTS " +
      "artist_tracking (artist_id STRING, artist_name STRING, nb_fan LONG, popularity LONG, date_maj DATE) " +
      "USING DELTA " +
      "PARTITIONED BY (date_maj) " +
      "LOCATION 'warehouse/delta/artist_tracking'")

    spark.sql("CREATE TABLE IF NOT EXISTS " +
      "music_tracking (music_id STRING, artist_id STRING, name STRING, duration DOUBLE, popularity LONG, date_maj DATE) " +
      "USING DELTA " +
      "PARTITIONED BY (date_maj) " +
      "LOCATION 'warehouse/delta/music_tracking'")


    spark.sql("CREATE TABLE IF NOT EXISTS " +
      "music_stats (artist_id STRING, nb_top_track LONG) " +
      "USING DELTA " +
      "LOCATION 'warehouse/delta/top_track_by_artist'")

  }
}
