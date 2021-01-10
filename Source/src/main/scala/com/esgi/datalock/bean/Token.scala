package com.esgi.datalock.bean

case class Token(

                  access_token: String,
                  token_type: String,
                  expires_in: String,
                  scope: String

                )