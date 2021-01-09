package com.yoji.likeshare.dto

import com.yoji.likeshare.application.App
import java.util.*

data class Post(
    val id: Long,
    val author: String,
    val avatar: Int,
    val published: Date,
    val content: String,
    val likedByMe: Boolean,
    val video: String? = null,
    var likesCounter: Int,
    var shareCounter: Int,
    var watchesCounter: Int
){
    companion object{
        val DEF_AVATAR_ID = App.applicationContext().resources.getIdentifier(
            "ic_default_user",
            "drawable",
            App.applicationContext().packageName
        )
    }
}