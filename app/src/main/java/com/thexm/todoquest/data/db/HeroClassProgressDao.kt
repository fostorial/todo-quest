package com.thexm.todoquest.data.db

import androidx.room.*
import com.thexm.todoquest.data.model.HeroClassProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface HeroClassProgressDao {

    @Query("SELECT * FROM hero_class_progress")
    fun getAllProgress(): Flow<List<HeroClassProgress>>

    @Query("SELECT * FROM hero_class_progress WHERE classId = :classId")
    suspend fun getProgressOnce(classId: String): HeroClassProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(progress: HeroClassProgress)

    @Query("DELETE FROM hero_class_progress")
    suspend fun clearAll()
}
