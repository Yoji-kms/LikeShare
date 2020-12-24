package com.yoji.likeshare.dto

data class Post(
    val id: Long,
    val author: String,
    val avatar: Int,
    val published: String,
    val content: String,
    val likedByMe: Boolean,
    val video: String? = null,
    var likesCounter: Int,
    var shareCounter: Int,
    var watchesCounter: Int
)