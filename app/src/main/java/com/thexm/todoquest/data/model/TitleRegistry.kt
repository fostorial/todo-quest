package com.thexm.todoquest.data.model

object TitleRegistry {

    val DEFAULT_TITLE_ID = "novice_adventurer"

    val ALL: List<Title> = listOf(
        // Default — always unlocked
        Title(
            id = "novice_adventurer",
            displayName = "Novice Adventurer",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Every legend begins somewhere."
        ),

        // PALTRY — Common (🪨)
        Title(
            id = "wandering_squire",
            displayName = "Wandering Squire",
            xpTier = XPTier.PALTRY,
            levelRequired = 5,
            flavorText = "A squire in search of purpose."
        ),
        Title(
            id = "village_guardian",
            displayName = "Village Guardian",
            xpTier = XPTier.PALTRY,
            levelRequired = 10,
            flavorText = "Protector of small but worthy things."
        ),
        Title(
            id = "errand_runner",
            displayName = "Errand Runner",
            xpTier = XPTier.PALTRY,
            levelRequired = 15,
            flavorText = "No task is too humble for a dedicated soul."
        ),

        // SMALL — Uncommon (⚔️)
        Title(
            id = "blade_initiate",
            displayName = "Blade Initiate",
            xpTier = XPTier.SMALL,
            levelRequired = 20,
            flavorText = "The blade is drawn. The journey sharpens."
        ),
        Title(
            id = "guild_recruit",
            displayName = "Guild Recruit",
            xpTier = XPTier.SMALL,
            levelRequired = 25,
            flavorText = "Welcomed into the ranks of the capable."
        ),
        Title(
            id = "sworn_seeker",
            displayName = "Sworn Seeker",
            xpTier = XPTier.SMALL,
            levelRequired = 30,
            flavorText = "Bound by oath to see every quest through."
        ),

        // MEDIUM — Rare (🗡️)
        Title(
            id = "seasoned_adventurer",
            displayName = "Seasoned Adventurer",
            xpTier = XPTier.MEDIUM,
            levelRequired = 35,
            flavorText = "Scars tell the stories words cannot."
        ),
        Title(
            id = "quest_knight",
            displayName = "Quest Knight",
            xpTier = XPTier.MEDIUM,
            levelRequired = 40,
            flavorText = "Knighted by deeds, not birth."
        ),
        Title(
            id = "dungeon_runner",
            displayName = "Dungeon Runner",
            xpTier = XPTier.MEDIUM,
            levelRequired = 45,
            flavorText = "Into the dark, and back again."
        ),

        // LARGE — Epic (🏆)
        Title(
            id = "battle_hardened_hero",
            displayName = "Battle-Hardened Hero",
            xpTier = XPTier.LARGE,
            levelRequired = 50,
            flavorText = "Forged in fire. Unyielding."
        ),
        Title(
            id = "champion_of_the_realm",
            displayName = "Champion of the Realm",
            xpTier = XPTier.LARGE,
            levelRequired = 55,
            flavorText = "The realm itself bows to your deeds."
        ),
        Title(
            id = "warden_of_quests",
            displayName = "Warden of Quests",
            xpTier = XPTier.LARGE,
            levelRequired = 60,
            flavorText = "Guardian of the sacred to-do list."
        ),

        // EPIC — Legendary (💎)
        Title(
            id = "mythic_champion",
            displayName = "Mythic Champion",
            xpTier = XPTier.EPIC,
            levelRequired = 65,
            flavorText = "Songs are sung of your triumphs."
        ),
        Title(
            id = "legendary_conqueror",
            displayName = "Legendary Conqueror",
            xpTier = XPTier.EPIC,
            levelRequired = 70,
            flavorText = "No quest stands unbeaten before you."
        ),
        Title(
            id = "transcendent_legend",
            displayName = "Transcendent Legend",
            xpTier = XPTier.EPIC,
            levelRequired = 75,
            flavorText = "Beyond mortal reckoning. A living myth."
        )
    )

    fun getById(id: String): Title = ALL.find { it.id == id } ?: ALL.first()

    fun unlockedFor(level: Int): List<Title> = ALL.filter { level >= it.levelRequired }

    fun isUnlocked(id: String, level: Int): Boolean = getById(id).levelRequired <= level

    /** Titles grouped by tier for display (null tier = default group). */
    fun groupedByTier(): Map<XPTier?, List<Title>> = ALL.groupBy { it.xpTier }
}
