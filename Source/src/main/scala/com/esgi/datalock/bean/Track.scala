package com.esgi.datalock.bean

import java.util.ArrayList

case class Track(

                 album: Album,
                 artists: ArrayList[SimpleArtist],
                 available_markets: ArrayList[String],
                 disc_number: Int,
                 duration_ms: Int,
                 href: String,
                 id: String,
                 name: String,
                 popularity: Int,
                 track_number: Int,
                 `type`: String,
                 uri: String

               )


