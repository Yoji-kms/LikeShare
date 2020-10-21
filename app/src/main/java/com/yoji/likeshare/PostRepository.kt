package com.yoji.likeshare

import androidx.lifecycle.LiveData

interface PostRepository {
    fun get(): LiveData<Post>
    fun like()
    fun share()
    fun randomCounters()
}