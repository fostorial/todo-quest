package com.thexm.todoquest.data.repository

import com.thexm.todoquest.data.db.HeroClassProgressDao
import com.thexm.todoquest.data.db.PlayerProfileDao
import com.thexm.todoquest.data.model.HeroClassProgress
import com.thexm.todoquest.data.model.PlayerProfile
import com.thexm.todoquest.data.model.XPTier
import com.thexm.todoquest.util.LevelCalculator
import kotlinx.coroutines.flow.Flow

class PlayerRepository(
    private val dao: PlayerProfileDao,
    private val classProgressDao: HeroClassProgressDao
) {

    fun getProfile(): Flow<PlayerProfile?> = dao.getProfile()

    fun getAllClassProgress(): kotlinx.coroutines.flow.Flow<List<HeroClassProgress>> =
        classProgressDao.getAllProgress()

    suspend fun getOrCreateProfile(): PlayerProfile {
        return dao.getProfileOnce() ?: PlayerProfile().also { dao.insertProfile(it) }
    }

    /**
     * Awards XP to the player's total and simultaneously banks it against
     * the currently active class so branch-unlock progress accumulates.
     */
    suspend fun awardXP(xpAmount: Int) {
        val profile = getOrCreateProfile()
        val newTotalXP = profile.totalXP + xpAmount
        val newLevel = LevelCalculator.levelForXP(newTotalXP)
        dao.updateProfile(profile.copy(totalXP = newTotalXP, level = newLevel))

        // Accumulate XP in the currently active class
        val current = classProgressDao.getProgressOnce(profile.selectedClassId)
            ?: HeroClassProgress(profile.selectedClassId, 0)
        classProgressDao.upsertProgress(current.copy(xpEarned = current.xpEarned + xpAmount))
    }

    /** Records a completed quest: increments total count and the per-tier count. */
    suspend fun recordQuestCompletion(tier: XPTier) {
        val profile = getOrCreateProfile()
        dao.updateProfile(
            profile.copy(
                totalQuestsCompleted = profile.totalQuestsCompleted + 1,
                paltryQuestsCompleted = profile.paltryQuestsCompleted + if (tier == XPTier.PALTRY) 1 else 0,
                smallQuestsCompleted  = profile.smallQuestsCompleted  + if (tier == XPTier.SMALL)  1 else 0,
                mediumQuestsCompleted = profile.mediumQuestsCompleted + if (tier == XPTier.MEDIUM) 1 else 0,
                largeQuestsCompleted  = profile.largeQuestsCompleted  + if (tier == XPTier.LARGE)  1 else 0,
                epicQuestsCompleted   = profile.epicQuestsCompleted   + if (tier == XPTier.EPIC)   1 else 0,
            )
        )
    }

    suspend fun updateProfile(profile: PlayerProfile) = dao.updateProfile(profile)

    suspend fun ensureProfileExists() { getOrCreateProfile() }

    suspend fun resetProfile() {
        val profile = getOrCreateProfile()
        dao.updateProfile(
            profile.copy(
                level = 1,
                totalXP = 0,
                totalQuestsCompleted = 0,
                paltryQuestsCompleted = 0,
                smallQuestsCompleted = 0,
                mediumQuestsCompleted = 0,
                largeQuestsCompleted = 0,
                epicQuestsCompleted = 0,
                selectedTitleId = "novice_adventurer",
                selectedBackgroundId = "stone_grey",
                selectedClassId = "adventurer"
            )
        )
        classProgressDao.clearAll()
    }
}
