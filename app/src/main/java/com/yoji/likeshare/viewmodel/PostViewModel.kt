package com.yoji.likeshare.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yoji.likeshare.*
import com.yoji.likeshare.application.App
import com.yoji.likeshare.dto.Post
import com.yoji.likeshare.repository.PostRepository
import com.yoji.likeshare.repository.PostRepositoryJsonImplementation

@SuppressLint("UseCompatLoadingForDrawables")
private val emptyPost = Post(
    id = 0,
    content = "",
    author = "",
    avatar = PostRepositoryJsonImplementation.DEF_AVATAR_ID,
    likedByMe = false,
    published = App.applicationContext().resources.getString(R.string.default_date),
    likesCounter = 0,
    shareCounter = 0,
    watchesCounter = 0
)

class PostViewModel(
    private val postRepository: PostRepository
) : ViewModel() {
    val data = postRepository.getAll()
    private val editingPost = MutableLiveData(emptyPost)

    fun likeById(id: Long) = postRepository.likeById(id)
    fun shareById(id: Long) = postRepository.shareById(id)
    fun removeById(id: Long) = postRepository.removeById(id)

    fun edit(post: Post) {
        editingPost.value = post
    }

    fun clear() {
        editingPost.value = emptyPost
    }

    fun save() {
        editingPost.value?.let { postRepository.save(it) }
        editingPost.value = emptyPost
    }

    fun changeContent(newContent: String) {
        val newContentTrimmed = newContent.trim()
        if (editingPost.value?.content == newContentTrimmed) {
            return
        }
        editingPost.value = editingPost.value?.copy(content = newContentTrimmed)
    }
}