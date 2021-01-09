package com.yoji.likeshare.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yoji.likeshare.entity.PostEntity

@Dao
interface PostRoomDAO {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Query(
        """
        UPDATE PostEntity SET
        likesCounter = likesCounter + CASE WHEN likedByMe THEN -1 ELSE 1 END,
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id
    """
    )
    fun likeById(id: Long)

    @Insert
    fun insert(post: PostEntity)

    @Query("UPDATE PostEntity SET shareCounter = shareCounter + 1 WHERE id = :id")
    fun shareById(id: Long)

    @Query("UPDATE PostEntity SET content = :content WHERE id = :id")
    fun updateContentById(id: Long, content: String)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    fun removeById(id: Long)

    fun save(post: PostEntity) =
        if (post.id == 0L) insert(post) else updateContentById(post.id, post.content)
}