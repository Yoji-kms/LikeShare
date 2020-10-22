package com.yoji.likeshare

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.random.Random

class PostRepositoryInMemoryImplementation : PostRepository {
    @SuppressLint("UseCompatLoadingForDrawables")
    private var posts = listOf(
        Post(
            id = 1,
            author = App.applicationContext().resources.getString(R.string.app_name),
            avatar = App.applicationContext().resources.getDrawable(R.drawable.ic_netology, null),
            published = App.applicationContext().resources.getString(R.string.some_date),
            content = App.applicationContext().resources.getString(R.string.content1),
            likedByMe = false,
            likesCounter = Random.nextInt(from = 0, until = 999),
            shareCounter = Random.nextInt(from = 0, until = 999),
            watchesCounter = Random.nextInt(from = 0, until = 999)
        ),
        Post(
            id = 2,
            author = App.applicationContext().resources.getString(R.string.app_name),
            avatar = App.applicationContext().resources.getDrawable(R.drawable.ic_netology, null),
            published = App.applicationContext().resources.getString(R.string.some_date2),
            content = App.applicationContext().resources.getString(R.string.content2),
            likedByMe = false,
            likesCounter = Random.nextInt(from = 0, until = 999),
            shareCounter = Random.nextInt(from = 0, until = 999),
            watchesCounter = Random.nextInt(from = 0, until = 999)
        )
    )
    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
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
        posts = posts.map {
            if (it.id == id) it.copy(shareCounter = it.shareCounter + 1) else it
        }
        data.value = posts
    }

    override fun randomCounters() {
        posts = posts.map {
            it.copy(
                likesCounter = Random.nextInt(from = 0, until = 999),
                shareCounter = Random.nextInt(from = 1_000, until = 999_999),
                watchesCounter = Random.nextInt(from = 1_000_000, until = Int.MAX_VALUE)
            )
        }
        data.value = posts
    }
}