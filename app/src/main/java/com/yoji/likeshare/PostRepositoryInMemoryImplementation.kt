package com.yoji.likeshare

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.random.Random

class PostRepositoryInMemoryImplementation : PostRepository {
    private var post = Post(
        id = 1,
        author = App.applicationContext().resources.getString(R.string.app_name),
        published = App.applicationContext().resources.getString(R.string.some_date),
        content = App.applicationContext().resources.getString(R.string.main_text),
        likedByMe = false,
        likesCounter = Random.nextInt(from = 0, until = 999),
        shareCounter = Random.nextInt(from = 0, until = 999),
        watchesCounter = Random.nextInt(from = 0, until = 999)
    )
    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data

    override fun like() {
        post =
            if (!post.likedByMe) post.copy(likedByMe = true, likesCounter = post.likesCounter + 1)
            else post.copy(likedByMe = false, likesCounter = post.likesCounter - 1)
        data.value = post
    }

    override fun share() {
        post = post.copy(shareCounter = post.shareCounter + 1)
        data.value = post
    }

    override fun randomCounters() {
        post = post.copy(
            likesCounter = Random.nextInt(from = 0, until = 999),
            shareCounter = Random.nextInt(from = 1_000, until = 999_999),
            watchesCounter = Random.nextInt(from = 1_000_000, until = Int.MAX_VALUE)
        )
        data.value = post
    }
}