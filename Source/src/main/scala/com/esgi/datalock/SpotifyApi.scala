package com.esgi.datalock

import com.esgi.datalock.conf.Configuration
import com.google.gson.Gson
import com.esgi.datalock.bean.{Artist, Token, TrackList}
import scalaj.http._

class SpotifyApi(conf: Configuration, artistId: String) {

  val accessToken: String = getAccesToken(getAuthorization)

  def getAuthorization: String ={

    val res: HttpResponse[String] = Http("https://accounts.spotify.com/api/token")
      .postForm(Seq("grant_type" -> conf.grant_type,
        "client_id" -> conf.client_id,
        "client_secret" -> conf.client_secret)).asString

    res.body

  }

  def getAccesToken(res: String): String ={

    val gson: Gson = new Gson()
    val token: Token = gson.fromJson(res, classOf[Token])
    token.access_token

  }

  def artistsRequest: Artist ={

    val res: HttpResponse[String] = Http("https://api.spotify.com/v1/artists/" + artistId)
      .header("Authorization",  "Bearer " + accessToken)
      .asString
    val gson: Gson = new Gson()
    gson.fromJson(res.body, classOf[Artist])

  }

  def trackListRequest: TrackList ={

    val res: HttpResponse[String] = Http("https://api.spotify.com/v1/artists/" + artistId + "/top-tracks?country=FR")
      .header("Authorization",  "Bearer " + accessToken)
      .asString
    val gson: Gson = new Gson()
    gson.fromJson(res.body, classOf[TrackList])

  }
}
