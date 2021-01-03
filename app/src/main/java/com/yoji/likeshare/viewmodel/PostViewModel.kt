package com.yoji.likeshare.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yoji.likeshare.*
import com.yoji.likeshare.application.App
import com.yoji.likeshare.db.AppDatabase
import com.yoji.likeshare.dto.Post
import com.yoji.likeshare.repository.PostRepository
import com.yoji.likeshare.repository.PostRepositorySqlDbImplementation

@SuppressLint("UseCompatLoadingForDrawables")
private val emptyPost = Post(
    id = 0L,
    content = "",
    author = "Me",
    avatar = Post.DEF_AVATAR_ID,
    likedByMe = false,
    published = App.applicationContext().resources.getString(R.string.default_date),
    likesCounter = 0,
    shareCounter = 0,
    watchesCounter = 0
)

class PostViewModel(
    private val postRepository: PostRepository = PostRepositorySqlDbImplementation(
        AppDatabase.getInstance(App.applicationContext()).postDAO
    )
) : ViewModel() {
    val data = postRepository.getAll()
    val selectedPost = MutableLiveData(emptyPost)

    fun likeById(id: Long) = postRepository.likeById(id)
    fun shareById(id: Long) = postRepository.shareById(id)
    fun removeById(id: Long) = postRepository.removeById(id)

    fun edit(post: Post) {
        selectedPost.value = post
    }

    fun clear() {
        selectedPost.value = emptyPost
    }

    fun save() {
        selectedPost.value?.let { postRepository.save(it) }
        selectedPost.value = emptyPost
    }

    fun changeContent(newContent: String) {
        val newContentTrimmed = newContent.trim()
        if (selectedPost.value?.content == newContentTrimmed) {
            return
        }
        selectedPost.value = selectedPost.value?.copy(content = newContentTrimmed)
    }
}