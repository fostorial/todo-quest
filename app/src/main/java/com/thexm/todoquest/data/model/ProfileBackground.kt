package com.thexm.todoquest.data.model

// ── Unlock requirements ───────────────────────────────────────────────────────

sealed class UnlockRequirement {

    abstract fun isMet(profile: PlayerProfile): Boolean
    abstract fun description(): String

    /** Two-param variant — only overridden by [ClassUnlocked]; all others delegate to the single-param version. */
    open fun isMet(profile: PlayerProfile, xpPerClass: Map<String, Long>): Boolean = isMet(profile)

    object AlwaysUnlocked : UnlockRequirement() {
        override fun isMet(profile: PlayerProfile) = true
        override fun description() = "Always unlocked"
    }

    data class ReachLevel(val level: Int) : UnlockRequirement() {
        override fun isMet(profile: PlayerProfile) = profile.level >= level
        override fun description() = "Reach Level $level"
    }

    data class EarnXP(val xp: Long) : UnlockRequirement() {
        override fun isMet(profile: PlayerProfile) = profile.totalXP >= xp
        override fun description() = "Earn ${xp.formatXP()} total XP"
    }

    data class CompleteQuests(val count: Int) : UnlockRequirement() {
        override fun isMet(profile: PlayerProfile) = profile.totalQuestsCompleted >= count
        override fun description() = "Complete $count quests"
    }

    data class CompleteTierQuests(val tier: XPTier, val count: Int) : UnlockRequirement() {
        override fun isMet(profile: PlayerProfile): Boolean {
            val completed = when (tier) {
                XPTier.PALTRY -> profile.paltryQuestsCompleted
                XPTier.SMALL  -> profile.smallQuestsCompleted
                XPTier.MEDIUM -> profile.mediumQuestsCompleted
                XPTier.LARGE  -> profile.largeQuestsCompleted
                XPTier.EPIC   -> profile.epicQuestsCompleted
            }
            return completed >= count
        }
        override fun description() = "Complete $count ${tier.displayName} quests"
    }

    /** Every listed requirement must be met. */
    data class All(val requirements: List<UnlockRequirement>) : UnlockRequirement() {
        override fun isMet(profile: PlayerProfile) = requirements.all { it.isMet(profile) }
        override fun description() = requirements.joinToString(" + ") { it.description() }
    }

    /** At least one listed requirement must be met. */
    data class Any(val requirements: List<UnlockRequirement>) : UnlockRequirement() {
        override fun isMet(profile: PlayerProfile) = requirements.any { it.isMet(profile) }
        override fun description() = requirements.joinToString(" or ") { it.description() }
    }

    /** Unlocked when the given hero class is unlocked (requires xpPerClass to evaluate). */
    data class ClassUnlocked(val classId: String) : UnlockRequirement() {
        override fun isMet(profile: PlayerProfile): Boolean = false  // always use the two-param version
        override fun isMet(profile: PlayerProfile, xpPerClass: Map<String, Long>): Boolean =
            HeroClassRegistry.isUnlocked(classId, xpPerClass)
        override fun description(): String = "Unlock the ${HeroClassRegistry.getById(classId).displayName} class"
    }
}

private fun Long.formatXP(): String = when {
    this >= 1_000 -> "${this / 1_000}k"
    else          -> "$this"
}

// ── Pattern types ─────────────────────────────────────────────────────────────

enum class PatternType {
    NONE,
    DIAGONAL_LINES,
    DIAGONAL_LINES_DENSE,
    GRID,
    DOTS,
    LARGE_DOTS,
    DIAMONDS,
    SCALES,
    CHEVRONS,
    STARS,
    HEXAGONS,
    WAVES,
    BRICKS,
    CROSSHATCH,
    TRIANGLES,
}

// ── Background model ──────────────────────────────────────────────────────────

data class ProfileBackground(
    val id: String,
    val displayName: String,
    val flavorText: String,
    val xpTier: XPTier?,                   // rarity colouring in the picker UI
    val unlockRequirement: UnlockRequirement,
    val gradientColors: List<Long>,         // stored as ARGB longs so the data class stays pure
    val patternType: PatternType,
    val patternAlpha: Float = 0.15f        // how visible the pattern overlay is
)
