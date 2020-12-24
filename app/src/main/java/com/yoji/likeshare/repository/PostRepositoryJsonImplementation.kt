package com.yoji.likeshare.repository

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yoji.likeshare.application.App
import com.yoji.likeshare.dto.Post
import com.yoji.likeshare.R

class PostRepositoryJsonImplementation : PostRepository {

    companion object {
        const val FILE = "posts.json"
        val context: Context = App.applicationContext()
        val DEF_AVATAR_ID = context.resources.getIdentifier(
            "ic_default_user",
            "drawable",
            App.applicationContext().packageName
        )
    }

    private val gson = Gson()
    private val file = context.filesDir.resolve(FILE)
    private val type = object : TypeToken<List<Post>>() {}.type
    private var posts = run {
        file
            .also { if (!it.exists()) return@run emptyList<Post>() }
            .readText()
            .ifBlank { return@run emptyList<Post>() }
            .let { gson.fromJson(it, type) }
    }

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
        sync()
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id == id) it.copy(shareCounter = it.shareCounter + 1) else it
        }
        data.value = posts
        sync()
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
        sync()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun save(post: Post) {
        posts = if (post.id == 0L) {
            listOf(
                post.copy(
                    id = (posts.lastIndex + 2).toLong(),
                    author = App.applicationContext().resources.getString(R.string.default_author),
                    avatar = DEF_AVATAR_ID,
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
        sync()
    }

    private fun sync() {
        file.writeText(gson.toJson(posts))
    }
}