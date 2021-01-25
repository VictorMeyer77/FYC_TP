package com.esgi.datalock

import com.esgi.datalock.utils.Checkpoint

object Main {

  def main(args: Array[String]): Unit ={

    Checkpoint.updateCheckpoint("artist",
      Checkpoint.getListFilesToProcess("artist",
        "/home/victor/esgi/M2/fyc/FYC_TP/FYC_TP/Source/output/artist"))

  }

}
