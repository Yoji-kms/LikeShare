package com.yoji.likeshare

data class Post(
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val likedByMe: Boolean,
    var likesCounter: Int,
    var shareCounter: Int,
    var watchesCounter: Int
)