package com.yoji.likeshare

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.random.Random

class PostRepositoryInMemoryImplementation : PostRepository {
    @SuppressLint("UseCompatLoadingForDrawables")
    private var posts = listOf(
        Post(
            id = 1,
            author = App.applicationContext().resources.getString(R.string.netology_author),
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
            author = App.applicationContext().resources.getString(R.string.netology_author),
            avatar = App.applicationContext().resources.getDrawable(R.drawable.ic_netology, null),
            published = App.applicationContext().resources.getString(R.string.some_date2),
            content = App.applicationContext().resources.getString(R.string.content2),
            likedByMe = false,
            likesCounter = Random.nextInt(from = 0, until = 999),
            shareCounter = Random.nextInt(from = 0, until = 999),
            watchesCounter = Random.nextInt(from = 0, until = 999)
        ),
        Post(
            id = 3,
            author = App.applicationContext().resources.getString(R.string.netology_author),
            avatar = App.applicationContext().resources.getDrawable(R.drawable.ic_netology, null),
            published = App.applicationContext().resources.getString(R.string.some_date2),
            content = "",
            likedByMe = false,
            video = "https://www.youtube.com/watch?v=pwHqY_4nsJ4",
            likesCounter = Random.nextInt(from = 0, until = 999),
            shareCounter = Random.nextInt(from = 1000, until = 99999),
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

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun save(post: Post) {
        posts = if (post.id == 0L){
            listOf(
                post.copy(
                    id = (posts.lastIndex + 2).toLong(),
                    author = App.applicationContext().resources.getString(R.string.default_author),
                    avatar = App.applicationContext().resources.getDrawable(
                        R.drawable.ic_default_user,
                        null
                    ),
                    likedByMe = false,
                    published = App.applicationContext().resources.getString(R.string.default_date)
                )
            ) + posts
        } else {
            posts.map {
                if (it.id != post.id) it else it.copy(content = post.content)
            }
        }
        data.value = posts
    }
}