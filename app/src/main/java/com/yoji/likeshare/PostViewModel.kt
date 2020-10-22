package com.yoji.likeshare

import androidx.lifecycle.ViewModel

class PostViewModel(
    private val postRepository: PostRepository = PostRepositoryInMemoryImplementation()
) : ViewModel() {
    val data = postRepository.getAll()
    fun likeById(id: Long) = postRepository.likeById(id)
    fun shareById(id: Long) = postRepository.shareById(id)
    fun randomCounters() = postRepository.randomCounters()
}