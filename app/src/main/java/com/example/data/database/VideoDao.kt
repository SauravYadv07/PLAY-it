package com.example.data.database

import androidx.room.*
import com.example.data.model.Bookmark
import com.example.data.model.CollectionEntity
import com.example.data.model.CollectionVideo
import com.example.data.model.HistoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    // Bookmarks
    @Query("SELECT * FROM bookmarks ORDER BY addedAt DESC")
    fun getAllBookmarks(): Flow<List<Bookmark>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: Bookmark)

    @Query("DELETE FROM bookmarks WHERE url = :url")
    suspend fun deleteBookmarkByUrl(url: String)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE url = :url LIMIT 1)")
    fun isBookmarked(url: String): Flow<Boolean>

    // History
    @Query("SELECT * FROM history_items ORDER BY playedAt DESC")
    fun getHistory(): Flow<List<HistoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(historyItem: HistoryItem)

    @Query("SELECT * FROM history_items WHERE url = :url LIMIT 1")
    suspend fun getHistoryItemByUrl(url: String): HistoryItem?

    @Query("DELETE FROM history_items WHERE id = :id")
    suspend fun deleteHistoryById(id: Int)

    @Query("DELETE FROM history_items")
    suspend fun clearHistory()

    // Collections (Custom Folders)
    @Query("SELECT * FROM collections ORDER BY name ASC")
    fun getAllCollections(): Flow<List<CollectionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollection(collection: CollectionEntity): Long

    @Query("DELETE FROM collections WHERE id = :id")
    suspend fun deleteCollection(id: Int)

    @Query("SELECT * FROM collection_videos WHERE collectionId = :collectionId ORDER BY addedAt DESC")
    fun getVideosInCollection(collectionId: Int): Flow<List<CollectionVideo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollectionVideo(video: CollectionVideo)

    @Query("DELETE FROM collection_videos WHERE id = :id")
    suspend fun deleteVideoFromCollection(id: Int)

    @Query("DELETE FROM collection_videos WHERE collectionId = :collectionId")
    suspend fun deleteVideosByCollectionId(collectionId: Int)
}
