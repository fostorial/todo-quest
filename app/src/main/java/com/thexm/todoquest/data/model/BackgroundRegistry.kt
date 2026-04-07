package com.thexm.todoquest.data.model

object BackgroundRegistry {

    val DEFAULT_ID = "stone_grey"

    val ALL: List<ProfileBackground> = listOf(

        // ── Default ───────────────────────────────────────────────────────────
        ProfileBackground(
            id = "stone_grey",
            displayName = "Stone Grey",
            flavorText = "The humble walls of every adventurer's starting inn.",
            xpTier = null,
            unlockRequirement = UnlockRequirement.AlwaysUnlocked,
            gradientColors = listOf(0xFF4B5563, 0xFF6B7280),
            patternType = PatternType.DIAGONAL_LINES
        ),

        // ── Common / Paltry ───────────────────────────────────────────────────
        ProfileBackground(
            id = "meadow_dawn",
            displayName = "Meadow Dawn",
            flavorText = "First light over quiet fields.",
            xpTier = XPTier.PALTRY,
            unlockRequirement = UnlockRequirement.ReachLevel(3),
            gradientColors = listOf(0xFF6EE7B7, 0xFF34D399, 0xFF059669),
            patternType = PatternType.DOTS
        ),
        ProfileBackground(
            id = "ocean_depths",
            displayName = "Ocean Depths",
            flavorText = "Where sunlight fades and mysteries dwell.",
            xpTier = XPTier.PALTRY,
            unlockRequirement = UnlockRequirement.ReachLevel(8),
            gradientColors = listOf(0xFF0EA5E9, 0xFF0369A1, 0xFF1E3A5F),
            patternType = PatternType.WAVES
        ),
        ProfileBackground(
            id = "ancient_parchment",
            displayName = "Ancient Parchment",
            flavorText = "Worn by countless quests and wandering hands.",
            xpTier = XPTier.PALTRY,
            unlockRequirement = UnlockRequirement.CompleteQuests(10),
            gradientColors = listOf(0xFFD4A96A, 0xFFC19A6B, 0xFFA07850),
            patternType = PatternType.CROSSHATCH
        ),
        ProfileBackground(
            id = "verdant_fields",
            displayName = "Verdant Fields",
            flavorText = "For those who never shy from a simple errand.",
            xpTier = XPTier.PALTRY,
            unlockRequirement = UnlockRequirement.CompleteTierQuests(XPTier.PALTRY, 10),
            gradientColors = listOf(0xFF86EFAC, 0xFF4ADE80, 0xFF16A34A),
            patternType = PatternType.LARGE_DOTS
        ),

        // ── Uncommon / Small ──────────────────────────────────────────────────
        ProfileBackground(
            id = "dungeon_stone",
            displayName = "Dungeon Stone",
            flavorText = "Carved by hands that didn't make it out.",
            xpTier = XPTier.SMALL,
            unlockRequirement = UnlockRequirement.ReachLevel(15),
            gradientColors = listOf(0xFF374151, 0xFF1F2937, 0xFF111827),
            patternType = PatternType.BRICKS
        ),
        ProfileBackground(
            id = "guild_banner",
            displayName = "Guild Banner",
            flavorText = "Colours of the sworn fellowship.",
            xpTier = XPTier.SMALL,
            unlockRequirement = UnlockRequirement.CompleteTierQuests(XPTier.SMALL, 10),
            gradientColors = listOf(0xFF3B82F6, 0xFF1D4ED8, 0xFFB45309),
            patternType = PatternType.DIAGONAL_LINES_DENSE
        ),
        ProfileBackground(
            id = "arcane_surge",
            displayName = "Arcane Surge",
            flavorText = "Magic crackles just beneath the surface.",
            xpTier = XPTier.SMALL,
            unlockRequirement = UnlockRequirement.EarnXP(500),
            gradientColors = listOf(0xFF818CF8, 0xFF6D28D9, 0xFF0D9488),
            patternType = PatternType.WAVES
        ),
        ProfileBackground(
            id = "midnight_sky",
            displayName = "Midnight Sky",
            flavorText = "A hundred quests, a thousand stars.",
            xpTier = XPTier.SMALL,
            unlockRequirement = UnlockRequirement.CompleteQuests(25),
            gradientColors = listOf(0xFF1E1B4B, 0xFF312E81, 0xFF0F172A),
            patternType = PatternType.STARS
        ),

        // ── Rare / Medium ─────────────────────────────────────────────────────
        ProfileBackground(
            id = "sunset_horizon",
            displayName = "Sunset Horizon",
            flavorText = "Every horizon is a quest not yet taken.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.ReachLevel(25),
            gradientColors = listOf(0xFFFB923C, 0xFFEC4899, 0xFF7C3AED),
            patternType = PatternType.CHEVRONS
        ),
        ProfileBackground(
            id = "arcane_weave",
            displayName = "Arcane Weave",
            flavorText = "Threads of magic spun into form.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.CompleteTierQuests(XPTier.MEDIUM, 10),
            gradientColors = listOf(0xFF6366F1, 0xFF4F46E5, 0xFF1E1B4B),
            patternType = PatternType.HEXAGONS
        ),
        ProfileBackground(
            id = "storm_mantle",
            displayName = "Storm Mantle",
            flavorText = "Worn by those who have weathered a thousand storms.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.EarnXP(1_000),
            gradientColors = listOf(0xFF64748B, 0xFF334155, 0xFF1E3A5F),
            patternType = PatternType.CROSSHATCH
        ),
        ProfileBackground(
            id = "autumn_harvest",
            displayName = "Autumn Harvest",
            flavorText = "The reward of fifty seasons of hard work.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.CompleteQuests(50),
            gradientColors = listOf(0xFFF97316, 0xFFB45309, 0xFF78350F),
            patternType = PatternType.DIAMONDS
        ),

        // ── Epic / Large ──────────────────────────────────────────────────────
        ProfileBackground(
            id = "ember_glow",
            displayName = "Ember Glow",
            flavorText = "The smouldering resolve of a veteran.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ReachLevel(40),
            gradientColors = listOf(0xFFEF4444, 0xFFB91C1C, 0xFF450A0A),
            patternType = PatternType.SCALES
        ),
        ProfileBackground(
            id = "warlords_crest",
            displayName = "Warlord's Crest",
            flavorText = "Claimed after battles most would not dare.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.CompleteTierQuests(XPTier.LARGE, 5),
            gradientColors = listOf(0xFF991B1B, 0xFF7F1D1D, 0xFF1C1917),
            patternType = PatternType.TRIANGLES
        ),
        ProfileBackground(
            id = "frozen_tundra",
            displayName = "Frozen Tundra",
            flavorText = "Silence so deep even the wind dares not speak.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.EarnXP(2_500),
            gradientColors = listOf(0xFFBAE6FD, 0xFF7DD3FC, 0xFF0EA5E9),
            patternType = PatternType.DIAMONDS
        ),
        ProfileBackground(
            id = "celestial_weave",
            displayName = "Celestial Weave",
            flavorText = "The pattern of the stars mapped onto one banner.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.All(
                listOf(
                    UnlockRequirement.ReachLevel(30),
                    UnlockRequirement.EarnXP(2_500)
                )
            ),
            gradientColors = listOf(0xFFFEF3C7, 0xFFFBBF24, 0xFF7C3AED),
            patternType = PatternType.STARS
        ),

        // ── Legendary / Epic ──────────────────────────────────────────────────
        ProfileBackground(
            id = "void_walker",
            displayName = "Void Walker",
            flavorText = "Beyond the edge of the known world.",
            xpTier = XPTier.EPIC,
            unlockRequirement = UnlockRequirement.ReachLevel(60),
            gradientColors = listOf(0xFF2E1065, 0xFF0C0A1A, 0xFF000000),
            patternType = PatternType.HEXAGONS
        ),
        ProfileBackground(
            id = "legendary_forge",
            displayName = "Legendary Forge",
            flavorText = "Hammered from the ore of three Epic victories.",
            xpTier = XPTier.EPIC,
            unlockRequirement = UnlockRequirement.CompleteTierQuests(XPTier.EPIC, 3),
            gradientColors = listOf(0xFFD97706, 0xFF92400E, 0xFF1C1917),
            patternType = PatternType.SCALES
        ),
        ProfileBackground(
            id = "nebula",
            displayName = "Nebula",
            flavorText = "Born from the dust of five thousand victories.",
            xpTier = XPTier.EPIC,
            unlockRequirement = UnlockRequirement.EarnXP(5_000),
            gradientColors = listOf(0xFF9333EA, 0xFFDB2777, 0xFF1E1B4B),
            patternType = PatternType.LARGE_DOTS
        ),
        ProfileBackground(
            id = "solar_flare",
            displayName = "Solar Flare",
            flavorText = "Ten thousand XP forged into blinding light.",
            xpTier = XPTier.EPIC,
            unlockRequirement = UnlockRequirement.EarnXP(10_000),
            gradientColors = listOf(0xFFFEF08A, 0xFFFBBF24, 0xFFEA580C),
            patternType = PatternType.CHEVRONS
        ),
        ProfileBackground(
            id = "champions_mantle",
            displayName = "Champion's Mantle",
            flavorText = "Awarded to those who have conquered the grandest and greatest alike.",
            xpTier = XPTier.EPIC,
            unlockRequirement = UnlockRequirement.All(
                listOf(
                    UnlockRequirement.CompleteTierQuests(XPTier.EPIC, 5),
                    UnlockRequirement.CompleteTierQuests(XPTier.LARGE, 5)
                )
            ),
            gradientColors = listOf(0xFFFCD34D, 0xFFD97706, 0xFF7C3AED),
            patternType = PatternType.SCALES
        )
    )

    fun getById(id: String): ProfileBackground = ALL.find { it.id == id } ?: ALL.first()

    fun unlockedFor(profile: com.thexm.todoquest.data.model.PlayerProfile): List<ProfileBackground> =
        ALL.filter { it.unlockRequirement.isMet(profile) }

    fun isUnlocked(id: String, profile: com.thexm.todoquest.data.model.PlayerProfile): Boolean =
        getById(id).unlockRequirement.isMet(profile)
}
