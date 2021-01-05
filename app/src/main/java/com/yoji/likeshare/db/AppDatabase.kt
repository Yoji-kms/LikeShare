package com.yoji.likeshare.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.yoji.likeshare.dao.PostDaoImplementation

class AppDatabase private constructor(db: SQLiteDatabase) {
    val postDAO = PostDaoImplementation(db)

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: AppDatabase (buildDatabase(context, arrayOf(PostDaoImplementation.DDL)))
            }
        }

        private fun buildDatabase(context: Context, DDLs: Array<String>) =
            DbHelper(context, 1, "app.db", DDLs).writableDatabase
    }
}