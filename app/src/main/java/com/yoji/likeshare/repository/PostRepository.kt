package com.yoji.likeshare.repository

import androidx.lifecycle.LiveData
import com.yoji.likeshare.dto.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)
}