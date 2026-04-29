package com.thexm.todoquest.data.model

object QuestBoardBackgroundRegistry {

    val ALL: List<ProfileBackground> = listOf(

        // ── Level Boards (every 3 levels, up to 60) ───────────────────────────

        // Common / Paltry — levels 3, 6, 9
        ProfileBackground(
            id = "qbb_lvl_3",
            displayName = "Cobblestone Path",
            flavorText = "The first steps of every legend begin here.",
            xpTier = XPTier.PALTRY,
            unlockRequirement = UnlockRequirement.ReachLevel(3),
            gradientColors = listOf(0xFF78716C, 0xFF57534E, 0xFF292524),
            patternType = PatternType.BRICKS,
            patternAlpha = 0.12f
        ),
        ProfileBackground(
            id = "qbb_lvl_6",
            displayName = "Meadow Mist",
            flavorText = "Dawn breaks over fields where heroes train.",
            xpTier = XPTier.PALTRY,
            unlockRequirement = UnlockRequirement.ReachLevel(6),
            gradientColors = listOf(0xFF86EFAC, 0xFF4ADE80, 0xFF166534),
            patternType = PatternType.DOTS,
            patternAlpha = 0.11f
        ),
        ProfileBackground(
            id = "qbb_lvl_9",
            displayName = "Riverside Camp",
            flavorText = "A quiet place to plan the next adventure.",
            xpTier = XPTier.PALTRY,
            unlockRequirement = UnlockRequirement.ReachLevel(9),
            gradientColors = listOf(0xFF38BDF8, 0xFF0369A1, 0xFF14532D),
            patternType = PatternType.WAVES,
            patternAlpha = 0.11f
        ),

        // Uncommon / Small — levels 12, 15, 18, 21
        ProfileBackground(
            id = "qbb_lvl_12",
            displayName = "Forest Hollow",
            flavorText = "Ancient trees watch over every task you set.",
            xpTier = XPTier.SMALL,
            unlockRequirement = UnlockRequirement.ReachLevel(12),
            gradientColors = listOf(0xFF14532D, 0xFF1A2E0A, 0xFF0A1505),
            patternType = PatternType.LARGE_DOTS,
            patternAlpha = 0.12f
        ),
        ProfileBackground(
            id = "qbb_lvl_15",
            displayName = "Dungeon Entry",
            flavorText = "Only the prepared dare venture further.",
            xpTier = XPTier.SMALL,
            unlockRequirement = UnlockRequirement.ReachLevel(15),
            gradientColors = listOf(0xFF374151, 0xFF1F2937, 0xFF111827),
            patternType = PatternType.CROSSHATCH,
            patternAlpha = 0.13f
        ),
        ProfileBackground(
            id = "qbb_lvl_18",
            displayName = "Crystal Cave",
            flavorText = "Quests glitter like gems in the dark.",
            xpTier = XPTier.SMALL,
            unlockRequirement = UnlockRequirement.ReachLevel(18),
            gradientColors = listOf(0xFF0D9488, 0xFF0E7490, 0xFF1E1B4B),
            patternType = PatternType.DIAMONDS,
            patternAlpha = 0.13f
        ),
        ProfileBackground(
            id = "qbb_lvl_21",
            displayName = "Mountain Pass",
            flavorText = "The higher you climb, the further you see.",
            xpTier = XPTier.SMALL,
            unlockRequirement = UnlockRequirement.ReachLevel(21),
            gradientColors = listOf(0xFF475569, 0xFF334155, 0xFF1E293B),
            patternType = PatternType.DIAGONAL_LINES,
            patternAlpha = 0.10f
        ),

        // Rare / Medium — levels 24, 27, 30, 33
        ProfileBackground(
            id = "qbb_lvl_24",
            displayName = "Stormfront",
            flavorText = "Power builds on the horizon. Stay focused.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.ReachLevel(24),
            gradientColors = listOf(0xFF1E3A5F, 0xFF1E293B, 0xFF0F172A),
            patternType = PatternType.CHEVRONS,
            patternAlpha = 0.12f
        ),
        ProfileBackground(
            id = "qbb_lvl_27",
            displayName = "Ember Wastes",
            flavorText = "Scorched by ambition, tempered by completion.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.ReachLevel(27),
            gradientColors = listOf(0xFFB45309, 0xFF78350F, 0xFF1C0500),
            patternType = PatternType.SCALES,
            patternAlpha = 0.13f
        ),
        ProfileBackground(
            id = "qbb_lvl_30",
            displayName = "Forgotten Ruins",
            flavorText = "Lost civilisations left their quests unfinished. You won't.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.ReachLevel(30),
            gradientColors = listOf(0xFF6B4226, 0xFF3D2100, 0xFF1A0B2E),
            patternType = PatternType.BRICKS,
            patternAlpha = 0.14f
        ),
        ProfileBackground(
            id = "qbb_lvl_33",
            displayName = "Twilight Vale",
            flavorText = "Where day and night strike a truce.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.ReachLevel(33),
            gradientColors = listOf(0xFF4F46E5, 0xFF7C3AED, 0xFF1E1B4B),
            patternType = PatternType.STARS,
            patternAlpha = 0.12f
        ),

        // Epic / Large — levels 36, 39, 42, 45
        ProfileBackground(
            id = "qbb_lvl_36",
            displayName = "Volcanic Rift",
            flavorText = "The earth itself cracks beneath unfinished tasks.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ReachLevel(36),
            gradientColors = listOf(0xFFDC2626, 0xFF7C2D12, 0xFF1C0500),
            patternType = PatternType.TRIANGLES,
            patternAlpha = 0.13f
        ),
        ProfileBackground(
            id = "qbb_lvl_39",
            displayName = "Frostpeak",
            flavorText = "Clarity comes at altitude. Ice never lies.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ReachLevel(39),
            gradientColors = listOf(0xFFBAE6FD, 0xFF38BDF8, 0xFF0369A1),
            patternType = PatternType.DIAGONAL_LINES_DENSE,
            patternAlpha = 0.10f
        ),
        ProfileBackground(
            id = "qbb_lvl_42",
            displayName = "Abyssal Depths",
            flavorText = "The deep rewards those who keep descending.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ReachLevel(42),
            gradientColors = listOf(0xFF0C4A6E, 0xFF0F172A, 0xFF020617),
            patternType = PatternType.HEXAGONS,
            patternAlpha = 0.13f
        ),
        ProfileBackground(
            id = "qbb_lvl_45",
            displayName = "Dragon's Lair",
            flavorText = "Every quest here is worth a dragon's ransom.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ReachLevel(45),
            gradientColors = listOf(0xFF991B1B, 0xFF78350F, 0xFFB45309),
            patternType = PatternType.SCALES,
            patternAlpha = 0.14f
        ),

        // Legendary / Epic — levels 48, 51, 54, 57, 60
        ProfileBackground(
            id = "qbb_lvl_48",
            displayName = "Astral Plane",
            flavorText = "Reality bends for those who reach this far.",
            xpTier = XPTier.EPIC,
            unlockRequirement = UnlockRequirement.ReachLevel(48),
            gradientColors = listOf(0xFF4C1D95, 0xFF6D28D9, 0xFF0F172A),
            patternType = PatternType.LARGE_DOTS,
            patternAlpha = 0.13f
        ),
        ProfileBackground(
            id = "qbb_lvl_51",
            displayName = "Void Rift",
            flavorText = "On the other side of every list — the void waits.",
            xpTier = XPTier.EPIC,
            unlockRequirement = UnlockRequirement.ReachLevel(51),
            gradientColors = listOf(0xFF2E1065, 0xFF0C0A1A, 0xFF000000),
            patternType = PatternType.CROSSHATCH,
            patternAlpha = 0.11f
        ),
        ProfileBackground(
            id = "qbb_lvl_54",
            displayName = "Celestial Spire",
            flavorText = "Quests written in starlight, completed in glory.",
            xpTier = XPTier.EPIC,
            unlockRequirement = UnlockRequirement.ReachLevel(54),
            gradientColors = listOf(0xFFFEF9C3, 0xFFFCD34D, 0xFF7C3AED),
            patternType = PatternType.STARS,
            patternAlpha = 0.14f
        ),
        ProfileBackground(
            id = "qbb_lvl_57",
            displayName = "Primordial Chaos",
            flavorText = "Where all quests began, before order existed.",
            xpTier = XPTier.EPIC,
            unlockRequirement = UnlockRequirement.ReachLevel(57),
            gradientColors = listOf(0xFF9333EA, 0xFFEC4899, 0xFFEA580C),
            patternType = PatternType.WAVES,
            patternAlpha = 0.12f
        ),
        ProfileBackground(
            id = "qbb_lvl_60",
            displayName = "Hero's Sanctum",
            flavorText = "Reserved for those who have truly earned the title.",
            xpTier = XPTier.EPIC,
            unlockRequirement = UnlockRequirement.ReachLevel(60),
            gradientColors = listOf(0xFFFCD34D, 0xFFD97706, 0xFF7C3AED),
            patternType = PatternType.HEXAGONS,
            patternAlpha = 0.14f
        ),

        // ── Quest Completion Boards ───────────────────────────────────────────

        // Paltry completions — humble, earthy, village
        ProfileBackground(
            id = "qbb_paltry_10",
            displayName = "Errand Runner's Nook",
            flavorText = "Small tasks, faithfully done.",
            xpTier = XPTier.PALTRY,
            unlockRequirement = UnlockRequirement.CompleteTierQuests(XPTier.PALTRY, 10),
            gradientColors = listOf(0xFFD4A96A, 0xFF92400E, 0xFF3D2100),
            patternType = PatternType.DOTS,
            patternAlpha = 0.11f
        ),
        ProfileBackground(
            id = "qbb_paltry_25",
            displayName = "Dusty Crossroads",
            flavorText = "A hundred small choices that add up to a journey.",
            xpTier = XPTier.PALTRY,
            unlockRequirement = UnlockRequirement.CompleteTierQuests(XPTier.PALTRY, 25),
            gradientColors = listOf(0xFFB45309, 0xFF78350F, 0xFF292524),
            patternType = PatternType.CROSSHATCH,
            patternAlpha = 0.11f
        ),
        ProfileBackground(
            id = "qbb_paltry_50",
            displayName = "Village Inn",
            flavorText = "Where diligent hands are always welcome.",
            xpTier = XPTier.SMALL,
            unlockRequirement = UnlockRequirement.CompleteTierQuests(XPTier.PALTRY, 50),
            gradientColors = listOf(0xFFD97706, 0xFF92400E, 0xFF451A03),
            patternType = PatternType.BRICKS,
            patternAlpha = 0.12f
        ),

        // Small completions — scouting, nature, frontier
        ProfileBackground(
            id = "qbb_small_10",
            displayName = "Trailblazer's Lodge",
            flavorText = "Those who scout ahead earn their rest.",
            xpTier = XPTier.SMALL,
            unlockRequirement = UnlockRequirement.CompleteTierQuests(XPTier.SMALL, 10),
            gradientColors = listOf(0xFF166534, 0xFF14532D, 0xFF1A1009),
            patternType = PatternType.LARGE_DOTS,
            patternAlpha = 0.12f
        ),
        ProfileBackground(
            id = "qbb_small_25",
            displayName = "Frontier Outpost",
            flavorText = "Carved out of the wilderness by persistence alone.",
            xpTier = XPTier.SMALL,
            unlockRequirement = UnlockRequirement.CompleteTierQuests(XPTier.SMALL, 25),
            gradientColors = listOf(0xFF0D9488, 0xFF065F46, 0xFF1A1009),
            patternType = PatternType.DIAGONAL_LINES,
            patternAlpha = 0.11f
        ),
        ProfileBackground(
            id = "qbb_small_50",
            displayName = "Waykeeper's Tower",
            flavorText = "Fifty paths forged. The road ahead is clear.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.CompleteTierQuests(XPTier.SMALL, 50),
            gradientColors = listOf(0xFF475569, 0xFF0F4C75, 0xFF0F172A),
            patternType = PatternType.CHEVRONS,
            patternAlpha = 0.12f
        ),

        // Medium completions — veteran, dungeon, warfare
        ProfileBackground(
            id = "qbb_medium_10",
            displayName = "Veteran's Barracks",
            flavorText = "Steel sharpened by ten proving grounds.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.CompleteTierQuests(XPTier.MEDIUM, 10),
            gradientColors = listOf(0xFF334155, 0xFF1E3A5F, 0xFF111827),
            patternType = PatternType.GRID,
            patternAlpha = 0.12f
        ),
        ProfileBackground(
            id = "qbb_medium_25",
            displayName = "War Room",
            flavorText = "Every quest plotted, every outcome weighed.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.CompleteTierQuests(XPTier.MEDIUM, 25),
            gradientColors = listOf(0xFF1E3A5F, 0xFF991B1B, 0xFF0F172A),
            patternType = PatternType.TRIANGLES,
            patternAlpha = 0.12f
        ),
        ProfileBackground(
            id = "qbb_medium_50",
            displayName = "Champion's Citadel",
            flavorText = "Fifty hard-won victories built these walls.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.CompleteTierQuests(XPTier.MEDIUM, 50),
            gradientColors = listOf(0xFF991B1B, 0xFF7F1D1D, 0xFFB45309),
            patternType = PatternType.SCALES,
            patternAlpha = 0.13f
        ),

        // Large completions — siege, conquest, fortresses
        ProfileBackground(
            id = "qbb_large_5",
            displayName = "Siege Ground",
            flavorText = "Where only the relentless dare to tread.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.CompleteTierQuests(XPTier.LARGE, 5),
            gradientColors = listOf(0xFFEA580C, 0xFF1F2937, 0xFF0C0A09),
            patternType = PatternType.DIAMONDS,
            patternAlpha = 0.13f
        ),
        ProfileBackground(
            id = "qbb_large_15",
            displayName = "Conqueror's Gate",
            flavorText = "Fifteen monumental deeds etched into the stone.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.CompleteTierQuests(XPTier.LARGE, 15),
            gradientColors = listOf(0xFF7F1D1D, 0xFF374151, 0xFF0C0A09),
            patternType = PatternType.BRICKS,
            patternAlpha = 0.14f
        ),
        ProfileBackground(
            id = "qbb_large_30",
            displayName = "Warlord's Throne Room",
            flavorText = "Thirty conquests. The throne was always yours.",
            xpTier = XPTier.EPIC,
            unlockRequirement = UnlockRequirement.CompleteTierQuests(XPTier.LARGE, 30),
            gradientColors = listOf(0xFF450A0A, 0xFF1C1917, 0xFFB45309),
            patternType = PatternType.HEXAGONS,
            patternAlpha = 0.13f
        ),

        // Epic completions — legendary, cosmic, immortal
        ProfileBackground(
            id = "qbb_epic_3",
            displayName = "Legend's Antechamber",
            flavorText = "The door to greatness opens at three.",
            xpTier = XPTier.EPIC,
            unlockRequirement = UnlockRequirement.CompleteTierQuests(XPTier.EPIC, 3),
            gradientColors = listOf(0xFF4C1D95, 0xFFD97706, 0xFF1E1B4B),
            patternType = PatternType.STARS,
            patternAlpha = 0.14f
        ),
        ProfileBackground(
            id = "qbb_epic_8",
            displayName = "Mythic Spire",
            flavorText = "Eight epic feats. Songs will be written.",
            xpTier = XPTier.EPIC,
            unlockRequirement = UnlockRequirement.CompleteTierQuests(XPTier.EPIC, 8),
            gradientColors = listOf(0xFF6D28D9, 0xFF0E7490, 0xFF050314),
            patternType = PatternType.WAVES,
            patternAlpha = 0.13f
        ),
        ProfileBackground(
            id = "qbb_epic_15",
            displayName = "Immortal Vault",
            flavorText = "Fifteen legends sealed within these walls forever.",
            xpTier = XPTier.EPIC,
            unlockRequirement = UnlockRequirement.CompleteTierQuests(XPTier.EPIC, 15),
            gradientColors = listOf(0xFF2E1065, 0xFF1C1917, 0xFFFCD34D),
            patternType = PatternType.CROSSHATCH,
            patternAlpha = 0.12f
        ),

        // Total quest completions
        ProfileBackground(
            id = "qbb_total_50",
            displayName = "Seasoned Board",
            flavorText = "Fifty quests of any stripe — the mark of a real adventurer.",
            xpTier = XPTier.SMALL,
            unlockRequirement = UnlockRequirement.CompleteQuests(50),
            gradientColors = listOf(0xFF0D9488, 0xFF78350F, 0xFF1F2937),
            patternType = PatternType.DIAGONAL_LINES_DENSE,
            patternAlpha = 0.10f
        ),
        ProfileBackground(
            id = "qbb_total_150",
            displayName = "Veteran's Study",
            flavorText = "One hundred and fifty tales, all in a day's work.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.CompleteQuests(150),
            gradientColors = listOf(0xFF1E3A5F, 0xFF78350F, 0xFF111827),
            patternType = PatternType.DIAMONDS,
            patternAlpha = 0.13f
        ),
        ProfileBackground(
            id = "qbb_total_500",
            displayName = "The Chronicler's Wall",
            flavorText = "Five hundred stories. Every one of them yours.",
            xpTier = XPTier.EPIC,
            unlockRequirement = UnlockRequirement.CompleteQuests(500),
            gradientColors = listOf(0xFFFCD34D, 0xFF334155, 0xFF0C0A09),
            patternType = PatternType.SCALES,
            patternAlpha = 0.14f
        ),

        // ── Tier 1 Class Boards ───────────────────────────────────────────────

        ProfileBackground(
            id = "qbb_warrior",
            displayName = "Iron Halls",
            flavorText = "Battle-worn stone carved by a thousand campaigns.",
            xpTier = XPTier.SMALL,
            unlockRequirement = UnlockRequirement.ClassUnlocked("warrior"),
            gradientColors = listOf(0xFF6B2626, 0xFF374151, 0xFF1C1917),
            patternType = PatternType.BRICKS,
            patternAlpha = 0.12f
        ),
        ProfileBackground(
            id = "qbb_mage",
            displayName = "Arcane Tower",
            flavorText = "Where knowledge accumulates and spells take form.",
            xpTier = XPTier.SMALL,
            unlockRequirement = UnlockRequirement.ClassUnlocked("mage"),
            gradientColors = listOf(0xFF1E1B4B, 0xFF2D1B69, 0xFF0D0B1A),
            patternType = PatternType.HEXAGONS,
            patternAlpha = 0.13f
        ),
        ProfileBackground(
            id = "qbb_rogue",
            displayName = "Shadow Market",
            flavorText = "Every quest here is off the record.",
            xpTier = XPTier.SMALL,
            unlockRequirement = UnlockRequirement.ClassUnlocked("rogue"),
            gradientColors = listOf(0xFF052E16, 0xFF0F172A, 0xFF030712),
            patternType = PatternType.DIAGONAL_LINES_DENSE,
            patternAlpha = 0.10f
        ),
        ProfileBackground(
            id = "qbb_cleric",
            displayName = "Blessed Grounds",
            flavorText = "Every task completed here echoes in the heavens.",
            xpTier = XPTier.SMALL,
            unlockRequirement = UnlockRequirement.ClassUnlocked("cleric"),
            gradientColors = listOf(0xFF78350F, 0xFFB45309, 0xFFFEF3C7),
            patternType = PatternType.STARS,
            patternAlpha = 0.11f
        ),

        // ── Tier 2 Class Boards ───────────────────────────────────────────────

        ProfileBackground(
            id = "qbb_knight",
            displayName = "Castle Ramparts",
            flavorText = "High stone walls and unwavering duty.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.ClassUnlocked("knight"),
            gradientColors = listOf(0xFF1E3A5F, 0xFF334155, 0xFF1F2937),
            patternType = PatternType.BRICKS,
            patternAlpha = 0.14f
        ),
        ProfileBackground(
            id = "qbb_berserker",
            displayName = "Battle Pit",
            flavorText = "Calm is a weakness. Get it done.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.ClassUnlocked("berserker"),
            gradientColors = listOf(0xFF7C2D12, 0xFFDC2626, 0xFF1C0500),
            patternType = PatternType.TRIANGLES,
            patternAlpha = 0.13f
        ),
        ProfileBackground(
            id = "qbb_pyromancer",
            displayName = "Scorched Earth",
            flavorText = "Obstacles don't stand. They burn.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.ClassUnlocked("pyromancer"),
            gradientColors = listOf(0xFFEA580C, 0xFFB91C1C, 0xFF1C0500),
            patternType = PatternType.SCALES,
            patternAlpha = 0.12f
        ),
        ProfileBackground(
            id = "qbb_necromancer",
            displayName = "Bone Crypt",
            flavorText = "Even finished quests linger here.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.ClassUnlocked("necromancer"),
            gradientColors = listOf(0xFF3B0764, 0xFF1A0B2E, 0xFF050314),
            patternType = PatternType.CROSSHATCH,
            patternAlpha = 0.11f
        ),
        ProfileBackground(
            id = "qbb_assassin",
            displayName = "Hidden Den",
            flavorText = "The best quests are the ones no one notices.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.ClassUnlocked("assassin"),
            gradientColors = listOf(0xFF111827, 0xFF0F1B12, 0xFF030712),
            patternType = PatternType.DIAGONAL_LINES,
            patternAlpha = 0.09f
        ),
        ProfileBackground(
            id = "qbb_ranger",
            displayName = "Woodland Trail",
            flavorText = "Every path is just a quest not yet mapped.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.ClassUnlocked("ranger"),
            gradientColors = listOf(0xFF14532D, 0xFF3D2100, 0xFF1A1009),
            patternType = PatternType.DOTS,
            patternAlpha = 0.14f
        ),
        ProfileBackground(
            id = "qbb_paladin",
            displayName = "Sacred Hall",
            flavorText = "Justice administered, one task at a time.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.ClassUnlocked("paladin"),
            gradientColors = listOf(0xFF1D4ED8, 0xFF1E3A8A, 0xFFFBBF24),
            patternType = PatternType.GRID,
            patternAlpha = 0.10f
        ),
        ProfileBackground(
            id = "qbb_druid",
            displayName = "Ancient Circle",
            flavorText = "Rooted in patience, grown through effort.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.ClassUnlocked("druid"),
            gradientColors = listOf(0xFF14532D, 0xFF1A2E0A, 0xFF0A1505),
            patternType = PatternType.WAVES,
            patternAlpha = 0.13f
        ),

        // ── Tier 3 Class Boards ───────────────────────────────────────────────

        ProfileBackground(
            id = "qbb_crusader",
            displayName = "Holy Bastion",
            flavorText = "A cause worth fighting for is worth organising for.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("crusader"),
            gradientColors = listOf(0xFFD97706, 0xFFB45309, 0xFF1E1B00),
            patternType = PatternType.STARS,
            patternAlpha = 0.14f
        ),
        ProfileBackground(
            id = "qbb_guardian",
            displayName = "Eternal Keep",
            flavorText = "Nothing leaves unfinished. Nothing ever will.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("guardian"),
            gradientColors = listOf(0xFF1F2937, 0xFF111827, 0xFF0C0A09),
            patternType = PatternType.BRICKS,
            patternAlpha = 0.15f
        ),
        ProfileBackground(
            id = "qbb_warlord",
            displayName = "Conquered Throne",
            flavorText = "Every quest on this board bows to your will.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("warlord"),
            gradientColors = listOf(0xFF991B1B, 0xFF7F1D1D, 0xFFB45309),
            patternType = PatternType.CHEVRONS,
            patternAlpha = 0.12f
        ),
        ProfileBackground(
            id = "qbb_juggernaut",
            displayName = "Iron Citadel",
            flavorText = "Immovable. Unstoppable. Uncompromising.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("juggernaut"),
            gradientColors = listOf(0xFF292524, 0xFF1C1917, 0xFF0C0A09),
            patternType = PatternType.GRID,
            patternAlpha = 0.13f
        ),
        ProfileBackground(
            id = "qbb_inferno_mage",
            displayName = "Lava Vault",
            flavorText = "Deadlines don't miss here — they incinerate.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("inferno_mage"),
            gradientColors = listOf(0xFFDC2626, 0xFF7C2D12, 0xFF1C0500),
            patternType = PatternType.SCALES,
            patternAlpha = 0.14f
        ),
        ProfileBackground(
            id = "qbb_phoenix",
            displayName = "Rebirth Spire",
            flavorText = "Every overdue quest rises anew from the ashes.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("phoenix"),
            gradientColors = listOf(0xFFF97316, 0xFFDC2626, 0xFF450A0A),
            patternType = PatternType.CHEVRONS,
            patternAlpha = 0.12f
        ),
        ProfileBackground(
            id = "qbb_lich",
            displayName = "Undying Archive",
            flavorText = "Tasks that outlast even death.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("lich"),
            gradientColors = listOf(0xFF4C1D95, 0xFF1E1B4B, 0xFF050314),
            patternType = PatternType.HEXAGONS,
            patternAlpha = 0.13f
        ),
        ProfileBackground(
            id = "qbb_soul_reaper",
            displayName = "Reaping Grounds",
            flavorText = "Each completion claimed, soul by soul.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("soul_reaper"),
            gradientColors = listOf(0xFF3B0764, 0xFF1E1B4B, 0xFF020005),
            patternType = PatternType.DIAGONAL_LINES_DENSE,
            patternAlpha = 0.10f
        ),
        ProfileBackground(
            id = "qbb_shadow_dancer",
            displayName = "Twilight Court",
            flavorText = "Graceful momentum in the darkest hours.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("shadow_dancer"),
            gradientColors = listOf(0xFF1E1B4B, 0xFF2D1B69, 0xFF030712),
            patternType = PatternType.WAVES,
            patternAlpha = 0.11f
        ),
        ProfileBackground(
            id = "qbb_phantom_blade",
            displayName = "Spectral Dojo",
            flavorText = "Quests completed before you even noticed them.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("phantom_blade"),
            gradientColors = listOf(0xFF475569, 0xFF334155, 0xFF0F172A),
            patternType = PatternType.DIAGONAL_LINES,
            patternAlpha = 0.10f
        ),
        ProfileBackground(
            id = "qbb_beastmaster",
            displayName = "Wild Enclosure",
            flavorText = "Tame every task like the creature it is.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("beastmaster"),
            gradientColors = listOf(0xFF78350F, 0xFF3D2100, 0xFF0A1505),
            patternType = PatternType.LARGE_DOTS,
            patternAlpha = 0.13f
        ),
        ProfileBackground(
            id = "qbb_sharpshooter",
            displayName = "Eagle's Eyrie",
            flavorText = "Every deadline in sight. None missed.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("sharpshooter"),
            gradientColors = listOf(0xFF0369A1, 0xFF065F46, 0xFF0F172A),
            patternType = PatternType.DIAMONDS,
            patternAlpha = 0.12f
        ),
        ProfileBackground(
            id = "qbb_holy_avenger",
            displayName = "Radiant Tribunal",
            flavorText = "Justice for every unfinished task.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("holy_avenger"),
            gradientColors = listOf(0xFFFEF9C3, 0xFFFCD34D, 0xFF92400E),
            patternType = PatternType.STARS,
            patternAlpha = 0.12f
        ),
        ProfileBackground(
            id = "qbb_battle_priest",
            displayName = "Sanctified Armory",
            flavorText = "Faith and discipline, side by side.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("battle_priest"),
            gradientColors = listOf(0xFF1D4ED8, 0xFF1E40AF, 0xFF92400E),
            patternType = PatternType.CROSSHATCH,
            patternAlpha = 0.11f
        ),
        ProfileBackground(
            id = "qbb_shapeshifter",
            displayName = "Shifting Lair",
            flavorText = "The board that adapts to whatever you need.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("shapeshifter"),
            gradientColors = listOf(0xFF0D9488, 0xFF4C1D95, 0xFF0F172A),
            patternType = PatternType.SCALES,
            patternAlpha = 0.13f
        ),
        ProfileBackground(
            id = "qbb_summoner",
            displayName = "Ethereal Nexus",
            flavorText = "Every quest answered by forces beyond one hero.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("summoner"),
            gradientColors = listOf(0xFF6D28D9, 0xFFDB2777, 0xFF1E1B4B),
            patternType = PatternType.HEXAGONS,
            patternAlpha = 0.12f
        )
    )

    fun getById(id: String): ProfileBackground? = ALL.find { it.id == id }

    fun unlockedFor(profile: PlayerProfile, xpPerClass: Map<String, Long> = emptyMap()): List<ProfileBackground> =
        ALL.filter { it.unlockRequirement.isMet(profile, xpPerClass) }

    fun isUnlocked(id: String, profile: PlayerProfile, xpPerClass: Map<String, Long> = emptyMap()): Boolean =
        getById(id)?.unlockRequirement?.isMet(profile, xpPerClass) ?: false
}
