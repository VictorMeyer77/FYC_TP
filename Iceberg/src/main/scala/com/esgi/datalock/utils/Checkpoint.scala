package com.esgi.datalock.utils

import java.io.{BufferedWriter, File, FileWriter}
import java.nio.file.{Files, Paths}
import com.esgi.datalock.bean.Flag
import com.google.gson.Gson
import scala.io.{BufferedSource, Source}

object Checkpoint {

  def checkpointExist(table: String): Boolean ={

    Files.exists(Paths.get("checkpoints/" + table + "/flag.json"))

  }

  def readFlag(table: String): Long ={

    val file: BufferedSource = Source.fromFile("checkpoints/" + table + "/flag.json")
    val fileContent: String = file.mkString
    file.close()
    val gson: Gson = new Gson()
    val jsonContent: Flag = gson.fromJson(fileContent, classOf[Flag])
    jsonContent.date

  }

  def getFlag(table: String): Long ={

    if (checkpointExist(table)) {
      readFlag(table)
    } else {
      0
    }

  }

  def getListOfFiles(dir: String): List[File] ={

    val d = new File(dir)

    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }

  }

  def getListFilesToProcess(table: String, filesPath: String): List[String] ={

    val flag: Long = getFlag(table)
    val listOfFiles: List[File] = getListOfFiles(filesPath)
    var filesToProcess: List[String] = List()

    listOfFiles.foreach(
      file => {

        val fileName: String = file.getName

        if(fileName != ".gitignore") {
          if (fileName.replace(".json", "").toLong > flag)
            filesToProcess = file.toString :: filesToProcess
        }
      }
    )

    filesToProcess

  }

  def updateCheckpoint(table: String, filesToProcess: List[String]): Unit ={


    if(filesToProcess.nonEmpty){

      val sortedList: List[String] = filesToProcess.sorted
      val lastFile: String = sortedList.last
      val date: Long = lastFile.split("/").last.replace(".json", "").toLong
      val flag: Flag = Flag(date)
      val gson: Gson = new Gson()
      val jsonContent: String = gson.toJson(flag)
      val file: File = new File("checkpoints/" + table + "/flag.json")
      file.createNewFile()
      val bufferedFile: BufferedWriter = new BufferedWriter(new FileWriter(file))
      bufferedFile.write(jsonContent)
      bufferedFile.close()

    }
  }

}
