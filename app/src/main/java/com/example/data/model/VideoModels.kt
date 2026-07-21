package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class Bookmark(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val url: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "history_items")
data class HistoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val url: String,
    val playedAt: Long = System.currentTimeMillis(),
    val lastPositionMs: Long = 0L,
    val durationMs: Long = 0L
)

@Entity(tableName = "collections")
data class CollectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "collection_videos")
data class CollectionVideo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val collectionId: Int,
    val title: String,
    val url: String,
    val duration: String = "",
    val addedAt: Long = System.currentTimeMillis()
)

data class CuratedVideo(
    val title: String,
    val description: String,
    val videoUrl: String,
    val thumbnailUrl: String,
    val category: String,
    val duration: String
)

data class LocalVideo(
    val id: Long,
    val title: String,
    val path: String,
    val duration: Long,
    val size: Long,
    val dateAdded: Long,
    val folderName: String
)
