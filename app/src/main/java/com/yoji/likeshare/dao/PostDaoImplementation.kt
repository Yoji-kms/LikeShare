package com.yoji.likeshare.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.core.database.getStringOrNull
import com.yoji.likeshare.db.PostColumns
import com.yoji.likeshare.dto.Post
import java.util.*

class PostDaoImplementation(private val db: SQLiteDatabase) : PostDAO {
    companion object {
        val DDL = """
            CREATE TABLE ${PostColumns.TABLE} (
            ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
            ${PostColumns.COLUMN_AVATAR} INTEGER NOT NULL,
            ${PostColumns.COLUMN_PUBLISHED} DATE NOT NULL,
            ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
            ${PostColumns.COLUMN_VIDEO} TEXT,
            ${PostColumns.COLUMN_LIKED_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_LIKES_COUNTER} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_SHARE_COUNTER} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_WATCHES_COUNTER} INTEGER NOT NULL DEFAULT 0
            )
        """.trimIndent()
    }

    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>()
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${PostColumns.COLUMN_ID} DESC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }
        return posts
    }

    override fun likeById(id: Long) {
        val post = getById(id)
        val values = ContentValues().apply {
            if (post.likedByMe) {
                put(PostColumns.COLUMN_LIKED_BY_ME, 0)
                put(
                    PostColumns.COLUMN_LIKES_COUNTER,
                    post.likesCounter - 1
                )
            } else {
                put(PostColumns.COLUMN_LIKED_BY_ME, 1)
                put(
                    PostColumns.COLUMN_LIKES_COUNTER,
                    post.likesCounter + 1
                )
            }
        }
        db.update(
            PostColumns.TABLE,
            values,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    override fun shareById(id: Long) {
        val post = getById(id)
        val values = ContentValues().apply {
            put(
                PostColumns.COLUMN_SHARE_COUNTER,
                post.shareCounter + 1
            )
        }
        db.update(
            PostColumns.TABLE,
            values,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    override fun removeById(id: Long) {
        db.delete(
            PostColumns.TABLE,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            if (post.id != 0L) put(PostColumns.COLUMN_ID, post.id)
            put(PostColumns.COLUMN_AUTHOR, post.author)
            put(PostColumns.COLUMN_AVATAR, post.avatar)
            put(PostColumns.COLUMN_PUBLISHED, post.published.time)
            put(PostColumns.COLUMN_CONTENT, post.content)
            put(PostColumns.COLUMN_VIDEO, post.video)
            put(PostColumns.COLUMN_LIKED_BY_ME, post.likedByMe)
            put(PostColumns.COLUMN_LIKES_COUNTER, post.likesCounter)
            put(PostColumns.COLUMN_SHARE_COUNTER, post.shareCounter)
            put(PostColumns.COLUMN_WATCHES_COUNTER, post.watchesCounter)
        }
        val id = db.replace(PostColumns.TABLE, null, values)
        return getById(id)
    }

    private fun map(cursor: Cursor): Post {
        with(cursor) {
            return Post(
                id = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
                author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
                avatar = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_AVATAR)),
                published = Date(getLong(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED))),
                content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
                likedByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKED_BY_ME)) != 0,
                video = getStringOrNull(getColumnIndexOrThrow(PostColumns.COLUMN_VIDEO)),
                likesCounter = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES_COUNTER)),
                shareCounter = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_SHARE_COUNTER)),
                watchesCounter = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_WATCHES_COUNTER))
            )
        }
    }

    private fun getById(id: Long): Post{
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        ).use {
            (it.moveToNext())
            return map(it)
        }
    }
}