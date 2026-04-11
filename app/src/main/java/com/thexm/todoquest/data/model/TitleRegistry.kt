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
        ),

        // ── Class Titles — unlock when the matching hero class is unlocked ──────

        // Tier 1
        Title(
            id = "class_warrior",
            displayName = "Wrathful Warrior",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Wrath made manifest. Every task a battle won.",
            classRequired = "warrior"
        ),
        Title(
            id = "class_mage",
            displayName = "Masterful Mage",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Magic mastered through meticulous effort.",
            classRequired = "mage"
        ),
        Title(
            id = "class_rogue",
            displayName = "Relentless Rogue",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Rules are merely recommendations.",
            classRequired = "rogue"
        ),
        Title(
            id = "class_cleric",
            displayName = "Consecrated Cleric",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Called and consecrated to a higher cause.",
            classRequired = "cleric"
        ),

        // Tier 2
        Title(
            id = "class_knight",
            displayName = "Keen Knight",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Keen of blade and keener of purpose.",
            classRequired = "knight"
        ),
        Title(
            id = "class_berserker",
            displayName = "Brutal Berserker",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Brutality and boundless energy, unleashed.",
            classRequired = "berserker"
        ),
        Title(
            id = "class_pyromancer",
            displayName = "Perilous Pyromancer",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Playing with fire — and winning.",
            classRequired = "pyromancer"
        ),
        Title(
            id = "class_necromancer",
            displayName = "Nefarious Necromancer",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Not even failure stays dead for long.",
            classRequired = "necromancer"
        ),
        Title(
            id = "class_assassin",
            displayName = "Adept Assassin",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Absolute precision. Always on target.",
            classRequired = "assassin"
        ),
        Title(
            id = "class_ranger",
            displayName = "Roaming Ranger",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Ranging far, returning with results.",
            classRequired = "ranger"
        ),
        Title(
            id = "class_paladin",
            displayName = "Pious Paladin",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Principled, purposeful, and unstoppable.",
            classRequired = "paladin"
        ),
        Title(
            id = "class_druid",
            displayName = "Devoted Druid",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Deeply devoted to the balance of all things.",
            classRequired = "druid"
        ),

        // Tier 3
        Title(
            id = "class_crusader",
            displayName = "Courageous Crusader",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Courage carries the cause to completion.",
            classRequired = "crusader"
        ),
        Title(
            id = "class_guardian",
            displayName = "Gallant Guardian",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Gallantry is its own reward.",
            classRequired = "guardian"
        ),
        Title(
            id = "class_warlord",
            displayName = "Wicked Warlord",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Wickedly efficient on every field of battle.",
            classRequired = "warlord"
        ),
        Title(
            id = "class_juggernaut",
            displayName = "Jolting Juggernaut",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Jolts through every obstacle without mercy.",
            classRequired = "juggernaut"
        ),
        Title(
            id = "class_inferno_mage",
            displayName = "Incendiary Inferno Mage",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Incinerates inaction. Everything burns brighter.",
            classRequired = "inferno_mage"
        ),
        Title(
            id = "class_phoenix",
            displayName = "Peerless Phoenix",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Peers into the ashes and rises again.",
            classRequired = "phoenix"
        ),
        Title(
            id = "class_lich",
            displayName = "Lethal Lich",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Life and death are just logistics.",
            classRequired = "lich"
        ),
        Title(
            id = "class_soul_reaper",
            displayName = "Sinister Soul Reaper",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Silently sweeping souls into the ledger.",
            classRequired = "soul_reaper"
        ),
        Title(
            id = "class_shadow_dancer",
            displayName = "Sly Shadow Dancer",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Slips through shadows, surfacing only in success.",
            classRequired = "shadow_dancer"
        ),
        Title(
            id = "class_phantom_blade",
            displayName = "Precise Phantom Blade",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Precision that phantom foes never see coming.",
            classRequired = "phantom_blade"
        ),
        Title(
            id = "class_beastmaster",
            displayName = "Bold Beastmaster",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Bold enough to tame any beast on the list.",
            classRequired = "beastmaster"
        ),
        Title(
            id = "class_sharpshooter",
            displayName = "Sure-Shot Sharpshooter",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Sure aim. Steady hand. Seldom misses.",
            classRequired = "sharpshooter"
        ),
        Title(
            id = "class_holy_avenger",
            displayName = "Hallowed Holy Avenger",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Hallowed by deeds that heaven itself applauds.",
            classRequired = "holy_avenger"
        ),
        Title(
            id = "class_battle_priest",
            displayName = "Brave Battle Priest",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Blessings and battle cries in equal measure.",
            classRequired = "battle_priest"
        ),
        Title(
            id = "class_shapeshifter",
            displayName = "Subtle Shapeshifter",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Subtly shifting to suit every situation.",
            classRequired = "shapeshifter"
        ),
        Title(
            id = "class_summoner",
            displayName = "Supreme Summoner",
            xpTier = null,
            levelRequired = 1,
            flavorText = "Summons strength from sources unseen.",
            classRequired = "summoner"
        )
    )

    fun getById(id: String): Title = ALL.find { it.id == id } ?: ALL.first()

    fun unlockedFor(level: Int, xpPerClass: Map<String, Long> = emptyMap()): List<Title> =
        ALL.filter { title ->
            if (title.classRequired != null) {
                HeroClassRegistry.isUnlocked(title.classRequired, xpPerClass)
            } else {
                level >= title.levelRequired
            }
        }

    fun isUnlocked(id: String, level: Int, xpPerClass: Map<String, Long> = emptyMap()): Boolean {
        val title = getById(id)
        return if (title.classRequired != null) {
            HeroClassRegistry.isUnlocked(title.classRequired, xpPerClass)
        } else {
            title.levelRequired <= level
        }
    }

    /** Level-based titles grouped by tier for display (null tier = default group). Excludes class titles. */
    fun groupedByTier(): Map<XPTier?, List<Title>> =
        ALL.filter { it.classRequired == null }.groupBy { it.xpTier }

    /** All class-unlockable titles. */
    fun classTitles(): List<Title> = ALL.filter { it.classRequired != null }
}
