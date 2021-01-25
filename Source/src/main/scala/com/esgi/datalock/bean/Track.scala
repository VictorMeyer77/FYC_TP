package com.esgi.datalock.bean

import java.util.ArrayList

case class Track(

                 artists: ArrayList[SimpleArtist],
                 duration_ms: Int,
                 id: String,
                 name: String,
                 popularity: Int,

               )


