package com.yoji.likeshare.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yoji.likeshare.dto.Post
import java.util.Date

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val avatar: Int,
    val published: Date,
    val content: String,
    val likedByMe: Boolean,
    val video: String? = null,
    var likesCounter: Int,
    var shareCounter: Int,
    var watchesCounter: Int
) {
    companion object {
        fun fromPost(post: Post): PostEntity = with(post) {
            PostEntity(
                id,
                author,
                avatar,
                published,
                content,
                likedByMe,
                video,
                likesCounter,
                shareCounter,
                watchesCounter
            )
        }
    }
}

fun PostEntity.toPost(): Post = Post(
    id,
    author,
    avatar,
    published,
    content,
    likedByMe,
    video,
    likesCounter,
    shareCounter,
    watchesCounter
)
