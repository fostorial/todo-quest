package com.thexm.todoquest.data.db

import androidx.room.*
import com.thexm.todoquest.data.model.Quest
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestDao {

    @Query("SELECT * FROM quests WHERE listId = :listId ORDER BY isCompleted ASC, isPinned DESC, sortOrder ASC, createdAtMillis DESC")
    fun getQuestsForList(listId: Long): Flow<List<Quest>>

    @Query("SELECT * FROM quests WHERE isCompleted = 0 ORDER BY isPinned DESC, CASE WHEN dueDateMillis IS NULL THEN 1 ELSE 0 END ASC, dueDateMillis ASC, createdAtMillis DESC")
    fun getActiveQuests(): Flow<List<Quest>>

    @Query("SELECT * FROM quests WHERE isCompleted = 0 AND (isPinned = 1 OR dueDateMillis IS NOT NULL) ORDER BY isPinned DESC, dueDateMillis ASC LIMIT 5")
    fun getPinnedAndUpcomingQuests(): Flow<List<Quest>>

    @Query("SELECT * FROM quests WHERE isCompleted = 0 AND dueDateMillis IS NOT NULL AND dueDateMillis >= :now ORDER BY dueDateMillis ASC LIMIT 20")
    fun getUpcomingQuests(now: Long): Flow<List<Quest>>

    @Query("SELECT * FROM quests WHERE isCompleted = 0 AND dueDateMillis IS NOT NULL AND dueDateMillis < :now ORDER BY dueDateMillis ASC")
    fun getOverdueQuests(now: Long): Flow<List<Quest>>

    @Query("SELECT COUNT(*) FROM quests WHERE isCompleted = 0")
    fun getActiveQuestCount(): Flow<Int>

    @Query("SELECT * FROM quests WHERE id = :id")
    suspend fun getQuestById(id: Long): Quest?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuest(quest: Quest): Long

    @Update
    suspend fun updateQuest(quest: Quest)

    @Delete
    suspend fun deleteQuest(quest: Quest)

    @Query("UPDATE quests SET isCompleted = 1, completedAtMillis = :completedAt WHERE id = :questId")
    suspend fun completeQuest(questId: Long, completedAt: Long = System.currentTimeMillis())

    @Query("DELETE FROM quests WHERE listId = :listId AND isCompleted = 1")
    suspend fun clearCompletedInList(listId: Long)

    @Query("SELECT * FROM quests WHERE isCompleted = 1 AND listId = :listId ORDER BY completedAtMillis DESC")
    fun getCompletedQuestsForList(listId: Long): Flow<List<Quest>>
}
