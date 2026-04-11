package com.thexm.todoquest.data.repository

import com.thexm.todoquest.data.db.QuestDao
import com.thexm.todoquest.data.db.QuestListDao
import com.thexm.todoquest.data.model.Quest
import com.thexm.todoquest.data.model.QuestList
import kotlinx.coroutines.flow.Flow

class QuestRepository(
    private val questDao: QuestDao,
    private val questListDao: QuestListDao
) {
    fun getAllLists(): Flow<List<QuestList>> = questListDao.getAllLists()
    fun getQuestsForList(listId: Long): Flow<List<Quest>> = questDao.getQuestsForList(listId)
    fun getActiveQuests(): Flow<List<Quest>> = questDao.getActiveQuests()
    fun getPinnedAndUpcomingQuests(): Flow<List<Quest>> = questDao.getPinnedAndUpcomingQuests()
    fun getActiveQuestCount(): Flow<Int> = questDao.getActiveQuestCount()
    fun getCompletedQuestsForList(listId: Long): Flow<List<Quest>> = questDao.getCompletedQuestsForList(listId)
    fun getUpcomingQuests(now: Long): Flow<List<Quest>> = questDao.getUpcomingQuests(now)
    fun getOverdueQuests(now: Long): Flow<List<Quest>> = questDao.getOverdueQuests(now)
    fun getNoDueDateQuests(): Flow<List<Quest>> = questDao.getNoDueDateQuests()

    suspend fun getListById(id: Long): QuestList? = questListDao.getListById(id)
    suspend fun getListByShareId(shareId: String): QuestList? = questListDao.getListByShareId(shareId)
    suspend fun getQuestById(id: Long): Quest? = questDao.getQuestById(id)
    suspend fun getQuestsForListOnce(listId: Long): List<Quest> = questDao.getQuestsForListOnce(listId)

    suspend fun insertList(questList: QuestList): Long = questListDao.insertList(questList)
    suspend fun updateList(questList: QuestList) = questListDao.updateList(questList)
    suspend fun deleteList(questList: QuestList) = questListDao.deleteList(questList)

    suspend fun insertQuest(quest: Quest): Long = questDao.insertQuest(quest)
    suspend fun updateQuest(quest: Quest) = questDao.updateQuest(quest)
    suspend fun deleteQuest(quest: Quest) = questDao.deleteQuest(quest)
    suspend fun completeQuest(questId: Long) = questDao.completeQuest(questId)
    suspend fun clearCompletedInList(listId: Long) = questDao.clearCompletedInList(listId)
}
