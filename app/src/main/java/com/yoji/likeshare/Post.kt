package com.yoji.likeshare

import android.graphics.drawable.Drawable

data class Post(
    val id: Long,
    val author: String,
    val avatar: Drawable,
    val published: String,
    val content: String,
    val likedByMe: Boolean,
    var likesCounter: Int,
    var shareCounter: Int,
    var watchesCounter: Int
)