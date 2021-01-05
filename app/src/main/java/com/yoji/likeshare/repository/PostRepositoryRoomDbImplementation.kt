package com.yoji.likeshare.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.yoji.likeshare.dao.PostRoomDAO
import com.yoji.likeshare.dto.Post
import com.yoji.likeshare.entity.PostEntity
import com.yoji.likeshare.entity.toPost

class PostRepositoryRoomDbImplementation(private val dao: PostRoomDAO) : PostRepository {

    override fun getAll(): LiveData<List<Post>> = dao.getAll().map {
        it.map(PostEntity::toPost)
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromPost(post))
    }
}