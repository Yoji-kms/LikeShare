package com.yoji.likeshare.db

object PostColumns {
    const val TABLE = "posts"
    const val COLUMN_ID = "id"
    const val COLUMN_AUTHOR = "author"
    const val COLUMN_AVATAR = "avatar"
    const val COLUMN_PUBLISHED = "published"
    const val COLUMN_CONTENT = "content"
    const val COLUMN_VIDEO = "video"
    const val COLUMN_LIKED_BY_ME = "likedByMe"
    const val COLUMN_LIKES_COUNTER = "likesCounter"
    const val COLUMN_SHARE_COUNTER = "shareCounter"
    const val COLUMN_WATCHES_COUNTER = "watchesCounter"
    val ALL_COLUMNS = arrayOf(
        COLUMN_ID,
        COLUMN_AUTHOR,
        COLUMN_AVATAR,
        COLUMN_PUBLISHED,
        COLUMN_CONTENT,
        COLUMN_VIDEO,
        COLUMN_LIKED_BY_ME,
        COLUMN_LIKES_COUNTER,
        COLUMN_SHARE_COUNTER,
        COLUMN_WATCHES_COUNTER
    )
}