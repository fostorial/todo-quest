package com.thexm.todoquest.data.db

import androidx.room.*
import com.thexm.todoquest.data.model.QuestList
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestListDao {

    @Query("SELECT * FROM quest_lists ORDER BY sortOrder ASC, createdAtMillis ASC")
    fun getAllLists(): Flow<List<QuestList>>

    @Query("SELECT * FROM quest_lists WHERE id = :id")
    suspend fun getListById(id: Long): QuestList?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(questList: QuestList): Long

    @Update
    suspend fun updateList(questList: QuestList)

    @Delete
    suspend fun deleteList(questList: QuestList)
}
