package com.esgi.datalock

import java.io.{File, PrintWriter}
import java.util.Calendar
import com.google.gson.{Gson, GsonBuilder}
import scala.io.{BufferedSource, Source}
import com.esgi.datalock.conf.Configuration

object Main {

  def main(args: Array[String]): Unit ={


    val conf: Configuration = readConf("conf/configuration.json")
    val spotifyApi: SpotifyApi = new SpotifyApi(conf, "7dGJo4pcD2V6oG8kP0tJRR")
    val gson: Gson = new Gson()

    writeJson(gson.toJson(spotifyApi.trackListRequest), "tracklist")
    writeJson(gson.toJson(spotifyApi.artistsRequest), "artist")


  }

  def readConf(confPath: String): Configuration ={

    val file: BufferedSource = Source.fromFile(confPath)
    val gson = new GsonBuilder().setPrettyPrinting().create()
    val conf: Configuration = gson.fromJson(file.mkString, classOf[Configuration])
    file.close()
    conf

  }

  def writeJson(json: String, fileName: String): Unit ={

    val now: String = Calendar.getInstance().getTime.getTime.toString
    val output: PrintWriter = new PrintWriter(new File("output/" + fileName + "_" + now + ".json"))
    output.write(json)
    output.close()

  }


}
