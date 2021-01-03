package com.yoji.likeshare.db

import com.yoji.likeshare.dto.Post

interface PostDAO {
    fun getAll() : List<Post>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post): Post
}