package com.yoji.likeshare.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yoji.likeshare.db.PostDAO
import com.yoji.likeshare.dto.Post

class PostRepositorySqlDbImplementation(private val dao: PostDAO) : PostRepository {
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        posts = dao.getAll()
        data.value = posts
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        dao.likeById(id)
        posts = posts.map {
            when {
                it.id == id && !it.likedByMe ->
                    it.copy(likedByMe = true, likesCounter = it.likesCounter + 1)
                it.id == id && it.likedByMe ->
                    it.copy(likedByMe = false, likesCounter = it.likesCounter - 1)
                else -> it
            }
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
        posts = posts.map {
            if (it.id == id) it.copy(shareCounter = it.shareCounter + 1) else it
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        posts = if (id == 0L) {
            listOf(saved) + posts
        }else {
            posts.map {
                if (it.id != id) it else saved
            }
        }
        data.value = posts
    }
}