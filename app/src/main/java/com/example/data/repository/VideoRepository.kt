package com.example.data.repository

import com.example.data.database.VideoDao
import com.example.data.model.Bookmark
import com.example.data.model.CollectionEntity
import com.example.data.model.CollectionVideo
import com.example.data.model.HistoryItem
import kotlinx.coroutines.flow.Flow

class VideoRepository(private val videoDao: VideoDao) {
    val allBookmarks: Flow<List<Bookmark>> = videoDao.getAllBookmarks()
    val allHistory: Flow<List<HistoryItem>> = videoDao.getHistory()
    val allCollections: Flow<List<CollectionEntity>> = videoDao.getAllCollections()

    suspend fun addBookmark(title: String, url: String) {
        videoDao.insertBookmark(Bookmark(title = title, url = url))
    }

    suspend fun removeBookmark(url: String) {
        videoDao.deleteBookmarkByUrl(url)
    }

    fun isBookmarked(url: String): Flow<Boolean> {
        return videoDao.isBookmarked(url)
    }

    suspend fun addToHistory(title: String, url: String, positionMs: Long, durationMs: Long) {
        val existing = videoDao.getHistoryItemByUrl(url)
        val newItem = HistoryItem(
            id = existing?.id ?: 0,
            title = title,
            url = url,
            playedAt = System.currentTimeMillis(),
            lastPositionMs = positionMs,
            durationMs = durationMs
        )
        videoDao.insertHistory(newItem)
    }

    suspend fun deleteHistoryById(id: Int) {
        videoDao.deleteHistoryById(id)
    }

    suspend fun clearHistory() {
        videoDao.clearHistory()
    }

    // Custom Collections/Folders
    fun getVideosInCollection(collectionId: Int): Flow<List<CollectionVideo>> {
        return videoDao.getVideosInCollection(collectionId)
    }

    suspend fun createCollection(name: String): Long {
        return videoDao.insertCollection(CollectionEntity(name = name))
    }

    suspend fun deleteCollection(collectionId: Int) {
        videoDao.deleteVideosByCollectionId(collectionId)
        videoDao.deleteCollection(collectionId)
    }

    suspend fun addVideoToCollection(collectionId: Int, title: String, url: String, duration: String = "") {
        videoDao.insertCollectionVideo(
            CollectionVideo(
                collectionId = collectionId,
                title = title,
                url = url,
                duration = duration
            )
        )
    }

    suspend fun removeVideoFromCollection(videoId: Int) {
        videoDao.deleteVideoFromCollection(videoId)
    }
}
