package com.yoji.likeshare.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yoji.likeshare.converters.Converters
import com.yoji.likeshare.dao.PostRoomDAO
import com.yoji.likeshare.entity.PostEntity

@Database(entities = [PostEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class PostRoomDB : RoomDatabase() {
    abstract fun postDAO(): PostRoomDAO

    companion object {
        @Volatile
        private var instance: PostRoomDB? = null

        fun getInstance(context: Context): PostRoomDB {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, PostRoomDB::class.java, "app.db")
                .allowMainThreadQueries()
                .build()
    }
}