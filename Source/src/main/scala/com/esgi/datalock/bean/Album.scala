package com.esgi.datalock.bean

import java.util.ArrayList

case class Album (

                   album_group: String,
                   album_type: String,
                   artists: ArrayList[SimpleArtist],
                   available_markets: ArrayList[String],
                   href: String,
                   id: String,
                   name: String,
                   release_date: String,
                   release_date_precision: String,
                   `type`: String,
                   uri: String,
                   total_tracks: Int

                  )
