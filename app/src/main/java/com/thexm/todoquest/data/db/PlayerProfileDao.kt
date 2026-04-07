package com.thexm.todoquest.data.db

import androidx.room.*
import com.thexm.todoquest.data.model.PlayerProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerProfileDao {

    @Query("SELECT * FROM player_profile WHERE id = 1")
    fun getProfile(): Flow<PlayerProfile?>

    @Query("SELECT * FROM player_profile WHERE id = 1")
    suspend fun getProfileOnce(): PlayerProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: PlayerProfile)

    @Update
    suspend fun updateProfile(profile: PlayerProfile)
}
