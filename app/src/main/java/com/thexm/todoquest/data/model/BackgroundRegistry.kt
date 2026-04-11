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
        ),

        // ── Class Backgrounds — Tier 1 (Uncommon) ────────────────────────────
        ProfileBackground(
            id = "bg_warrior",
            displayName = "Iron & Blood",
            flavorText = "Forged in the crucible of a thousand battles.",
            xpTier = XPTier.SMALL,
            unlockRequirement = UnlockRequirement.ClassUnlocked("warrior"),
            gradientColors = listOf(0xFFDC2626, 0xFF7F1D1D, 0xFF374151),
            patternType = PatternType.TRIANGLES
        ),
        ProfileBackground(
            id = "bg_mage",
            displayName = "Arcane Depths",
            flavorText = "Knowledge crystallised into colour and light.",
            xpTier = XPTier.SMALL,
            unlockRequirement = UnlockRequirement.ClassUnlocked("mage"),
            gradientColors = listOf(0xFF312E81, 0xFF4C1D95, 0xFF0D0B1A),
            patternType = PatternType.HEXAGONS
        ),
        ProfileBackground(
            id = "bg_rogue",
            displayName = "Shadow Veil",
            flavorText = "Seen only when they want to be seen.",
            xpTier = XPTier.SMALL,
            unlockRequirement = UnlockRequirement.ClassUnlocked("rogue"),
            gradientColors = listOf(0xFF064E3B, 0xFF14532D, 0xFF0F172A),
            patternType = PatternType.DIAGONAL_LINES_DENSE
        ),
        ProfileBackground(
            id = "bg_cleric",
            displayName = "Sacred Dawn",
            flavorText = "The warmth of conviction at first light.",
            xpTier = XPTier.SMALL,
            unlockRequirement = UnlockRequirement.ClassUnlocked("cleric"),
            gradientColors = listOf(0xFFFEF3C7, 0xFFFBBF24, 0xFFD97706),
            patternType = PatternType.STARS
        ),

        // ── Class Backgrounds — Tier 2 (Rare) ────────────────────────────────
        ProfileBackground(
            id = "bg_knight",
            displayName = "Castle Ramparts",
            flavorText = "Stone laid by hands that swore to hold the line.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.ClassUnlocked("knight"),
            gradientColors = listOf(0xFF475569, 0xFF334155, 0xFF1E40AF),
            patternType = PatternType.BRICKS
        ),
        ProfileBackground(
            id = "bg_berserker",
            displayName = "Rage Surge",
            flavorText = "Fury given form. Calm is a distant memory.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.ClassUnlocked("berserker"),
            gradientColors = listOf(0xFFEA580C, 0xFFDC2626, 0xFF450A0A),
            patternType = PatternType.CHEVRONS
        ),
        ProfileBackground(
            id = "bg_pyromancer",
            displayName = "Ember Fields",
            flavorText = "Where obstacles stood, only ash remains.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.ClassUnlocked("pyromancer"),
            gradientColors = listOf(0xFFFB923C, 0xFFEF4444, 0xFF1C0500),
            patternType = PatternType.SCALES
        ),
        ProfileBackground(
            id = "bg_necromancer",
            displayName = "Cursed Crypt",
            flavorText = "Even finished tasks whisper from beneath.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.ClassUnlocked("necromancer"),
            gradientColors = listOf(0xFF4B0082, 0xFF1A0B2E, 0xFF000000),
            patternType = PatternType.CROSSHATCH
        ),
        ProfileBackground(
            id = "bg_assassin",
            displayName = "Night's Edge",
            flavorText = "The darkness isn't empty — it watches.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.ClassUnlocked("assassin"),
            gradientColors = listOf(0xFF0F1113, 0xFF0F2418, 0xFF030712),
            patternType = PatternType.DIAGONAL_LINES
        ),
        ProfileBackground(
            id = "bg_ranger",
            displayName = "Forest Canopy",
            flavorText = "Every trail is just a quest not yet named.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.ClassUnlocked("ranger"),
            gradientColors = listOf(0xFF14532D, 0xFF166534, 0xFF3D2100),
            patternType = PatternType.LARGE_DOTS
        ),
        ProfileBackground(
            id = "bg_paladin",
            displayName = "Divine Thunder",
            flavorText = "Righteousness, armoured in lightning.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.ClassUnlocked("paladin"),
            gradientColors = listOf(0xFF1D4ED8, 0xFF7C3AED, 0xFFFBBF24),
            patternType = PatternType.STARS
        ),
        ProfileBackground(
            id = "bg_druid",
            displayName = "Ancient Grove",
            flavorText = "Roots older than memory, patience older than stone.",
            xpTier = XPTier.MEDIUM,
            unlockRequirement = UnlockRequirement.ClassUnlocked("druid"),
            gradientColors = listOf(0xFF15803D, 0xFF166534, 0xFF14532D),
            patternType = PatternType.WAVES
        ),

        // ── Class Backgrounds — Tier 3 (Epic) ────────────────────────────────
        ProfileBackground(
            id = "bg_crusader",
            displayName = "Holy Crusade",
            flavorText = "A cause worth dying for is worth living for.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("crusader"),
            gradientColors = listOf(0xFFFCD34D, 0xFFFBBF24, 0xFFD97706),
            patternType = PatternType.STARS
        ),
        ProfileBackground(
            id = "bg_guardian",
            displayName = "Eternal Fortress",
            flavorText = "Nothing passes. Nothing ever has.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("guardian"),
            gradientColors = listOf(0xFF374151, 0xFF1F2937, 0xFF111827),
            patternType = PatternType.BRICKS
        ),
        ProfileBackground(
            id = "bg_warlord",
            displayName = "Conqueror's Throne",
            flavorText = "Claimed from those who dared to resist.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("warlord"),
            gradientColors = listOf(0xFFB91C1C, 0xFF991B1B, 0xFFD97706),
            patternType = PatternType.DIAMONDS
        ),
        ProfileBackground(
            id = "bg_juggernaut",
            displayName = "Immovable",
            flavorText = "The mountain does not negotiate.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("juggernaut"),
            gradientColors = listOf(0xFF292524, 0xFF1C1917, 0xFF0C0A09),
            patternType = PatternType.CROSSHATCH
        ),
        ProfileBackground(
            id = "bg_inferno_mage",
            displayName = "Volcanic Fury",
            flavorText = "Every overdue task burns brighter than the last.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("inferno_mage"),
            gradientColors = listOf(0xFFDC2626, 0xFFEA580C, 0xFF1C0500),
            patternType = PatternType.SCALES
        ),
        ProfileBackground(
            id = "bg_phoenix",
            displayName = "Phoenix Rising",
            flavorText = "Fallen behind? Rise. Always rise.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("phoenix"),
            gradientColors = listOf(0xFFFBBF24, 0xFFF97316, 0xFFDC2626),
            patternType = PatternType.CHEVRONS
        ),
        ProfileBackground(
            id = "bg_lich",
            displayName = "Lich's Sanctum",
            flavorText = "Mastery over tasks even death could not finish.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("lich"),
            gradientColors = listOf(0xFF7C3AED, 0xFF3D0066, 0xFF0D0B1A),
            patternType = PatternType.HEXAGONS
        ),
        ProfileBackground(
            id = "bg_soul_reaper",
            displayName = "Soul Harvest",
            flavorText = "Every completed quest is a soul claimed.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("soul_reaper"),
            gradientColors = listOf(0xFF4C1D95, 0xFF1E1B4B, 0xFF050314),
            patternType = PatternType.DIAGONAL_LINES_DENSE
        ),
        ProfileBackground(
            id = "bg_shadow_dancer",
            displayName = "Twilight Waltz",
            flavorText = "Graceful. Lethal. Invisible.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("shadow_dancer"),
            gradientColors = listOf(0xFF2D1B69, 0xFF1A0B2E, 0xFF030712),
            patternType = PatternType.WAVES
        ),
        ProfileBackground(
            id = "bg_phantom_blade",
            displayName = "Phantom Realm",
            flavorText = "The quest was done before you knew it began.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("phantom_blade"),
            gradientColors = listOf(0xFF94A3B8, 0xFF64748B, 0xFF1E293B),
            patternType = PatternType.DIAGONAL_LINES
        ),
        ProfileBackground(
            id = "bg_beastmaster",
            displayName = "Wild Hunt",
            flavorText = "Every task is just another creature to tame.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("beastmaster"),
            gradientColors = listOf(0xFF78350F, 0xFF3D2100, 0xFF14532D),
            patternType = PatternType.DOTS
        ),
        ProfileBackground(
            id = "bg_sharpshooter",
            displayName = "Eagle's Sky",
            flavorText = "Aim for the deadline. Never miss.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("sharpshooter"),
            gradientColors = listOf(0xFF0EA5E9, 0xFF0369A1, 0xFF065F46),
            patternType = PatternType.DOTS
        ),
        ProfileBackground(
            id = "bg_holy_avenger",
            displayName = "Radiant Covenant",
            flavorText = "Justice delivered, one overdue quest at a time.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("holy_avenger"),
            gradientColors = listOf(0xFFFEF9C3, 0xFFFCD34D, 0xFFEA580C),
            patternType = PatternType.STARS
        ),
        ProfileBackground(
            id = "bg_battle_priest",
            displayName = "Sanctified Steel",
            flavorText = "Blessed steel and unwavering conviction.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("battle_priest"),
            gradientColors = listOf(0xFF1D4ED8, 0xFF1E40AF, 0xFFD97706),
            patternType = PatternType.GRID
        ),
        ProfileBackground(
            id = "bg_shapeshifter",
            displayName = "Dragon's Scale",
            flavorText = "Adapt to any quest, any obstacle, any form.",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("shapeshifter"),
            gradientColors = listOf(0xFF0D9488, 0xFF0F766E, 0xFF4C1D95),
            patternType = PatternType.SCALES
        ),
        ProfileBackground(
            id = "bg_summoner",
            displayName = "Ethereal Rift",
            flavorText = "Why do alone what an army can do together?",
            xpTier = XPTier.LARGE,
            unlockRequirement = UnlockRequirement.ClassUnlocked("summoner"),
            gradientColors = listOf(0xFF9333EA, 0xFFDB2777, 0xFF4C1D95),
            patternType = PatternType.WAVES
        )
    )

    fun getById(id: String): ProfileBackground = ALL.find { it.id == id } ?: ALL.first()

    fun unlockedFor(profile: PlayerProfile, xpPerClass: Map<String, Long> = emptyMap()): List<ProfileBackground> =
        ALL.filter { it.unlockRequirement.isMet(profile, xpPerClass) }

    fun isUnlocked(id: String, profile: PlayerProfile, xpPerClass: Map<String, Long> = emptyMap()): Boolean =
        getById(id).unlockRequirement.isMet(profile, xpPerClass)
}
