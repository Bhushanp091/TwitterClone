package com.example.twitterclone.main.domain

data class Comment(
    val comment: String = "",
    val imageUrl: String = "",
    val timeStamp: String = "",
    val userId: String = "",
    val postId: String = ""
)
