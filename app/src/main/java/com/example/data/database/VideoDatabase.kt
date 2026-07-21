package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.Bookmark
import com.example.data.model.CollectionEntity
import com.example.data.model.CollectionVideo
import com.example.data.model.HistoryItem

@Database(
    entities = [
        Bookmark::class,
        HistoryItem::class,
        CollectionEntity::class,
        CollectionVideo::class
    ],
    version = 2,
    exportSchema = false
)
abstract class VideoDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao

    companion object {
        @Volatile
        private var INSTANCE: VideoDatabase? = null

        fun getDatabase(context: Context): VideoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VideoDatabase::class.java,
                    "video_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
