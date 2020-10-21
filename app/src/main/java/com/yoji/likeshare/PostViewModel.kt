package com.yoji.likeshare

import androidx.lifecycle.ViewModel

class PostViewModel(
    private val postRepository: PostRepository = PostRepositoryInMemoryImplementation()
) : ViewModel() {
    val data = postRepository.get()
    fun like() = postRepository.like()
    fun share() = postRepository.share()
    fun randomCounters() = postRepository.randomCounters()
}