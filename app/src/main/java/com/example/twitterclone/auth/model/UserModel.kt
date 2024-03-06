package com.example.twitterclone.auth.model

data class UserModel(
   val  email: String = "",
   val password: String="",
   val bio: String="",
   val username: String="",
   val name:String="",
   val imageUri: String="",
   val  uid: String=""
)
