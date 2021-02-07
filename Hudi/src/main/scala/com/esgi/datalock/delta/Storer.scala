package com.esgi.datalock.delta

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.hudi.QuickstartUtils._
import scala.collection.JavaConversions._
import org.apache.spark.sql.SaveMode._
import org.apache.hudi.DataSourceReadOptions._
import org.apache.hudi.DataSourceWriteOptions._
import org.apache.hudi.config.HoodieWriteConfig._

class Storer(spark: SparkSession) {

  // table function

  def addDateMajCol(df: DataFrame): DataFrame ={

    df.withColumn("date_maj", current_date())

  }

  def addUid(df: DataFrame): DataFrame ={

    import spark.implicits._
    df.withColumn("uuid", expr("uuid()"))

  }

  // artist table functions

  def renameArtistCol(df: DataFrame): DataFrame ={

    df.withColumnRenamed("followers", "nb_fan")
      .withColumnRenamed("id", "artist_id")
      .withColumnRenamed("name", "artist_name")

  }

  def explodeFollowers(df: DataFrame): DataFrame ={

    df.withColumn("followers", explode(array("followers.total")))

  }

  // tracklist function

  def explodeArtist(df: DataFrame): DataFrame ={

    df.withColumn("artist_id", explode(array(col("artists.id")).getItem(0)))

  }

  def convertMillis(df: DataFrame): DataFrame ={

    df.withColumn("duration", col("duration_ms") / 1000)

  }

  def removeColumns(df: DataFrame): DataFrame ={

    df.drop(col("artists")).drop(col("duration_ms"))

  }

  def renameTrackCol(df: DataFrame): DataFrame ={

    df.withColumnRenamed("id", "music_id")

  }

  def countTopTrackByArtist(df: DataFrame): Unit ={

    val filterDf: DataFrame = df.filter("popularity >= 75")
      .groupBy("artist_id")
      .count()
      .withColumnRenamed("count", "nb_top_track")

    val finalDf: DataFrame = addUid(filterDf)

    finalDf.write
      .format("hudi")
      .option(PRECOMBINE_FIELD_OPT_KEY, "artist_id")
      .option(RECORDKEY_FIELD_OPT_KEY, "uuid")
      .option(PARTITIONPATH_FIELD_OPT_KEY, "partitionpath")
      .option(TABLE_NAME, "top_track_by_artist")
      .mode(Overwrite)
      .save("file:///tmp/hudi/top_track_by_artist")

  }

  def store(filePath: String, table: String): Unit ={

    val basePath = "file:///tmp/hudi/"
    val df = spark.read
      .format("org.apache.spark.sql.execution.datasources.v2.json.JsonDataSourceV2")
      .load(filePath)

    if(table == "artist"){

      val formatColDf: DataFrame = explodeFollowers(df)
      val renameColDf: DataFrame = renameArtistCol(formatColDf)
      val finalDf: DataFrame = addUid(addDateMajCol(renameColDf))

      finalDf.write
        .format("hudi")
        .option(PRECOMBINE_FIELD_OPT_KEY, "date_maj")
        .option(RECORDKEY_FIELD_OPT_KEY, "uuid")
        .option(PARTITIONPATH_FIELD_OPT_KEY, "partitionpath")
        .option(TABLE_NAME, table)
        .mode(Append)
        .save(basePath + table)

    }
    else if(table == "tracklist"){

      val formatColDf: DataFrame = explodeArtist(df)
      val fullDf: DataFrame = convertMillis(formatColDf)
      val cleanDf: DataFrame = removeColumns(fullDf)
      val renameColDf: DataFrame = renameTrackCol(cleanDf)
      val finalDf: DataFrame = addUid(addDateMajCol(renameColDf))

      finalDf.write
        .format("hudi")
        .option(PRECOMBINE_FIELD_OPT_KEY, "date_maj")
        .option(RECORDKEY_FIELD_OPT_KEY, "uuid")
        .option(PARTITIONPATH_FIELD_OPT_KEY, "partitionpath")
        .option(TABLE_NAME, table)
        .mode(Append)
        .save(basePath + table)

      countTopTrackByArtist(finalDf)

    }
  }
}
