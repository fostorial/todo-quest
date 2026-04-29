package com.thexm.todoquest.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.thexm.todoquest.data.model.HeroClassProgress
import com.thexm.todoquest.data.model.PlayerProfile
import com.thexm.todoquest.data.model.Quest
import com.thexm.todoquest.data.model.QuestList

@Database(
    entities = [Quest::class, QuestList::class, PlayerProfile::class, HeroClassProgress::class],
    version = 6,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class QuestDatabase : RoomDatabase() {

    abstract fun questDao(): QuestDao
    abstract fun questListDao(): QuestListDao
    abstract fun playerProfileDao(): PlayerProfileDao
    abstract fun heroClassProgressDao(): HeroClassProgressDao

    companion object {
        @Volatile private var INSTANCE: QuestDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE player_profile ADD COLUMN selectedTitleId TEXT NOT NULL DEFAULT 'novice_adventurer'"
                )
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE player_profile ADD COLUMN paltryQuestsCompleted INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE player_profile ADD COLUMN smallQuestsCompleted INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE player_profile ADD COLUMN mediumQuestsCompleted INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE player_profile ADD COLUMN largeQuestsCompleted INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE player_profile ADD COLUMN epicQuestsCompleted INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE player_profile ADD COLUMN selectedBackgroundId TEXT NOT NULL DEFAULT 'stone_grey'")
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE player_profile ADD COLUMN selectedClassId TEXT NOT NULL DEFAULT 'adventurer'")
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS hero_class_progress (
                        classId TEXT NOT NULL PRIMARY KEY,
                        xpEarned INTEGER NOT NULL DEFAULT 0
                    )
                """.trimIndent())
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE quest_lists ADD COLUMN shareId TEXT")
            }
        }

        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE quest_lists ADD COLUMN boardBackgroundId TEXT")
            }
        }

        fun getInstance(context: Context): QuestDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    QuestDatabase::class.java,
                    "quest_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
                    .build().also { INSTANCE = it }
            }
    }
}
