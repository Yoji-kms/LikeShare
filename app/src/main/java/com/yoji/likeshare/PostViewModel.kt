package com.yoji.likeshare

import androidx.lifecycle.ViewModel

class PostViewModel : ViewModel() {
    private val postRepository: PostRepository = PostRepositoryInMemoryImplementation()
    val data = postRepository.get()
    fun like() = postRepository.like()
    fun share() = postRepository.share()
    fun randomCounters() = postRepository.randomCounters()
}