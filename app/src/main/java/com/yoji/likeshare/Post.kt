package com.yoji.likeshare

import android.graphics.drawable.Drawable
import android.net.Uri

data class Post(
    val id: Long,
    val author: String,
    val avatar: Drawable,
    val published: String,
    val content: String,
    val likedByMe: Boolean,
    val video: String? = null,
    var likesCounter: Int,
    var shareCounter: Int,
    var watchesCounter: Int
)