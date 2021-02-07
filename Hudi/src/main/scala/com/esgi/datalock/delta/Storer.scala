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

  def store(filePath: String, table: String): Unit ={

    val df = spark.read
      .format("org.apache.spark.sql.execution.datasources.v2.json.JsonDataSourceV2")
      .load(filePath)

    if(table == "artist"){

      val formatColDf: DataFrame = explodeFollowers(df)
      val renameColDf: DataFrame = renameArtistCol(formatColDf)
      val finalDf: DataFrame = addDateMajCol(renameColDf)
      val tableName : String = "artist"
      val partitionPath :String = "../hudi/artist"
      val basePath = "/home/littlesoap/Document/Cours/fyc/warehouse/hudi/CasPratique/FYC_TP/Hudi/warehouse/hudi" 

      finalDf.write
        .mode("Overwrite")
        .format("hudi")
        .partitionBy("date_maj")
        .options(getQuickstartWriteConfigs)
        .option(PRECOMBINE_FIELD_OPT_KEY, "ts")
        .option(RECORDKEY_FIELD_OPT_KEY, "uuid")
        .option(PARTITIONPATH_FIELD_OPT_KEY, "partitionpath")
        .option(TABLE_NAME, tableName)
        .save(basePath)

    }
    else if(table == "tracklist"){

      val formatColDf: DataFrame = explodeArtist(df)
      val fullDf: DataFrame = convertMillis(formatColDf)
      val cleanDf: DataFrame = removeColumns(fullDf)
      val renameColDf: DataFrame = renameTrackCol(cleanDf)
      val finalDf: DataFrame = addDateMajCol(renameColDf)
      val tableName : String = "tracklist"
      val partitionPath :String = "../hudi/traclist"
      val basePath = "/home/littlesoap/Document/Cours/fyc/warehouse/hudi/CasPratique/FYC_TP/Hudi/warehouse/hudi"

      finalDf.write
        .mode("Overwrite")
        .format("hudi")
        .partitionBy("date_maj")
        .options(getQuickstartWriteConfigs)
        .option(PRECOMBINE_FIELD_OPT_KEY, "ts")
        .option(RECORDKEY_FIELD_OPT_KEY, "uuid")
        .option(PARTITIONPATH_FIELD_OPT_KEY, "partitionpath")
        .option(TABLE_NAME, tableName)
        .save(basePath)

    }
  }
}
