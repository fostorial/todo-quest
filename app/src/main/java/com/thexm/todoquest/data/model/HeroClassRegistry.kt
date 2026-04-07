package com.thexm.todoquest.data.model

object HeroClassRegistry {

    const val ROOT_ID = "adventurer"

    // XP thresholds for each tier transition
    private const val XP_T0_TO_T1 = 50L    // XP as Adventurer to unlock Tier 1
    private const val XP_T1_TO_T2 = 150L   // XP as a Tier-1 class to unlock its branches
    private const val XP_T2_TO_T3 = 350L   // XP as a Tier-2 class to unlock its branches

    val ALL: List<HeroClass> = listOf(

        // ── Tier 0 ────────────────────────────────────────────────────────────
        HeroClass(
            id = "adventurer", displayName = "Adventurer",
            description = "Every legend starts with a single quest.",
            emoji = "🧭", tier = 0, parentId = null, xpRequiredInParent = 0
        ),

        // ── Tier 1 ────────────────────────────────────────────────────────────
        HeroClass(
            id = "warrior", displayName = "Warrior",
            description = "Strength and discipline above all else.",
            emoji = "⚔️", tier = 1, parentId = "adventurer", xpRequiredInParent = XP_T0_TO_T1
        ),
        HeroClass(
            id = "mage", displayName = "Mage",
            description = "Knowledge is the sharpest blade.",
            emoji = "🔮", tier = 1, parentId = "adventurer", xpRequiredInParent = XP_T0_TO_T1
        ),
        HeroClass(
            id = "rogue", displayName = "Rogue",
            description = "Patience, precision, and a quiet step.",
            emoji = "🗡️", tier = 1, parentId = "adventurer", xpRequiredInParent = XP_T0_TO_T1
        ),
        HeroClass(
            id = "cleric", displayName = "Cleric",
            description = "Faith and purpose, unwavering.",
            emoji = "✨", tier = 1, parentId = "adventurer", xpRequiredInParent = XP_T0_TO_T1
        ),

        // ── Tier 2 : Warrior branches ─────────────────────────────────────────
        HeroClass(
            id = "knight", displayName = "Knight",
            description = "Honour bound, never broken.",
            emoji = "🛡️", tier = 2, parentId = "warrior", xpRequiredInParent = XP_T1_TO_T2
        ),
        HeroClass(
            id = "berserker", displayName = "Berserker",
            description = "Fury channelled into unstoppable force.",
            emoji = "💢", tier = 2, parentId = "warrior", xpRequiredInParent = XP_T1_TO_T2
        ),

        // ── Tier 2 : Mage branches ────────────────────────────────────────────
        HeroClass(
            id = "pyromancer", displayName = "Pyromancer",
            description = "Where others see obstacles, you see fuel.",
            emoji = "🔥", tier = 2, parentId = "mage", xpRequiredInParent = XP_T1_TO_T2
        ),
        HeroClass(
            id = "necromancer", displayName = "Necromancer",
            description = "Even completed tasks may yet serve a purpose.",
            emoji = "💀", tier = 2, parentId = "mage", xpRequiredInParent = XP_T1_TO_T2
        ),

        // ── Tier 2 : Rogue branches ───────────────────────────────────────────
        HeroClass(
            id = "assassin", displayName = "Assassin",
            description = "Strike swiftly. Leave no trace.",
            emoji = "🌑", tier = 2, parentId = "rogue", xpRequiredInParent = XP_T1_TO_T2
        ),
        HeroClass(
            id = "ranger", displayName = "Ranger",
            description = "The horizon is merely the next quest marker.",
            emoji = "🏹", tier = 2, parentId = "rogue", xpRequiredInParent = XP_T1_TO_T2
        ),

        // ── Tier 2 : Cleric branches ──────────────────────────────────────────
        HeroClass(
            id = "paladin", displayName = "Paladin",
            description = "Righteousness forged into armour.",
            emoji = "⚡", tier = 2, parentId = "cleric", xpRequiredInParent = XP_T1_TO_T2
        ),
        HeroClass(
            id = "druid", displayName = "Druid",
            description = "Harmony with every task, however small.",
            emoji = "🌿", tier = 2, parentId = "cleric", xpRequiredInParent = XP_T1_TO_T2
        ),

        // ── Tier 3 : Knight branches ──────────────────────────────────────────
        HeroClass(
            id = "crusader", displayName = "Crusader",
            description = "A holy cause demands extraordinary deeds.",
            emoji = "⚜️", tier = 3, parentId = "knight", xpRequiredInParent = XP_T2_TO_T3
        ),
        HeroClass(
            id = "guardian", displayName = "Guardian",
            description = "Nothing shall pass while you still stand.",
            emoji = "🏰", tier = 3, parentId = "knight", xpRequiredInParent = XP_T2_TO_T3
        ),

        // ── Tier 3 : Berserker branches ───────────────────────────────────────
        HeroClass(
            id = "warlord", displayName = "Warlord",
            description = "Conqueror of lists and legions alike.",
            emoji = "👑", tier = 3, parentId = "berserker", xpRequiredInParent = XP_T2_TO_T3
        ),
        HeroClass(
            id = "juggernaut", displayName = "Juggernaut",
            description = "Immovable. Unstoppable. Undeniable.",
            emoji = "🪨", tier = 3, parentId = "berserker", xpRequiredInParent = XP_T2_TO_T3
        ),

        // ── Tier 3 : Pyromancer branches ─────────────────────────────────────
        HeroClass(
            id = "inferno_mage", displayName = "Inferno Mage",
            description = "Every overdue task burns brighter than the last.",
            emoji = "🌋", tier = 3, parentId = "pyromancer", xpRequiredInParent = XP_T2_TO_T3
        ),
        HeroClass(
            id = "phoenix", displayName = "Phoenix",
            description = "Fallen behind? Rise. Always rise.",
            emoji = "🦅", tier = 3, parentId = "pyromancer", xpRequiredInParent = XP_T2_TO_T3
        ),

        // ── Tier 3 : Necromancer branches ────────────────────────────────────
        HeroClass(
            id = "lich", displayName = "Lich",
            description = "Mastery over tasks even death could not finish.",
            emoji = "🦴", tier = 3, parentId = "necromancer", xpRequiredInParent = XP_T2_TO_T3
        ),
        HeroClass(
            id = "soul_reaper", displayName = "Soul Reaper",
            description = "Every completed quest is a soul claimed.",
            emoji = "⚰️", tier = 3, parentId = "necromancer", xpRequiredInParent = XP_T2_TO_T3
        ),

        // ── Tier 3 : Assassin branches ────────────────────────────────────────
        HeroClass(
            id = "shadow_dancer", displayName = "Shadow Dancer",
            description = "Graceful. Lethal. Invisible.",
            emoji = "🌒", tier = 3, parentId = "assassin", xpRequiredInParent = XP_T2_TO_T3
        ),
        HeroClass(
            id = "phantom_blade", displayName = "Phantom Blade",
            description = "The quest was done before you knew it began.",
            emoji = "👤", tier = 3, parentId = "assassin", xpRequiredInParent = XP_T2_TO_T3
        ),

        // ── Tier 3 : Ranger branches ──────────────────────────────────────────
        HeroClass(
            id = "beastmaster", displayName = "Beastmaster",
            description = "Every task is just another creature to tame.",
            emoji = "🐺", tier = 3, parentId = "ranger", xpRequiredInParent = XP_T2_TO_T3
        ),
        HeroClass(
            id = "sharpshooter", displayName = "Sharpshooter",
            description = "Aim for the deadline. Never miss.",
            emoji = "🎯", tier = 3, parentId = "ranger", xpRequiredInParent = XP_T2_TO_T3
        ),

        // ── Tier 3 : Paladin branches ─────────────────────────────────────────
        HeroClass(
            id = "holy_avenger", displayName = "Holy Avenger",
            description = "Justice delivered, one overdue quest at a time.",
            emoji = "☀️", tier = 3, parentId = "paladin", xpRequiredInParent = XP_T2_TO_T3
        ),
        HeroClass(
            id = "battle_priest", displayName = "Battle Priest",
            description = "Blessed steel and unwavering conviction.",
            emoji = "🔯", tier = 3, parentId = "paladin", xpRequiredInParent = XP_T2_TO_T3
        ),

        // ── Tier 3 : Druid branches ───────────────────────────────────────────
        HeroClass(
            id = "shapeshifter", displayName = "Shapeshifter",
            description = "Adapt to any quest, any obstacle, any form.",
            emoji = "🐉", tier = 3, parentId = "druid", xpRequiredInParent = XP_T2_TO_T3
        ),
        HeroClass(
            id = "summoner", displayName = "Summoner",
            description = "Why do alone what an army can do together?",
            emoji = "🌀", tier = 3, parentId = "druid", xpRequiredInParent = XP_T2_TO_T3
        )
    )

    private val byId: Map<String, HeroClass> = ALL.associateBy { it.id }
    private val childrenOf: Map<String?, List<HeroClass>> = ALL.groupBy { it.parentId }

    fun getById(id: String): HeroClass = byId[id] ?: ALL.first()

    fun childrenOf(parentId: String): List<HeroClass> = childrenOf[parentId] ?: emptyList()

    /**
     * Returns true if [classId] is unlocked given the map of [xpPerClass].
     * A class is unlocked if the player has earned >= its [xpRequiredInParent] XP
     * while the parent class was active.
     */
    fun isUnlocked(classId: String, xpPerClass: Map<String, Long>): Boolean {
        val cls = getById(classId)
        if (cls.parentId == null) return true           // root is always unlocked
        val earnedInParent = xpPerClass[cls.parentId] ?: 0L
        return earnedInParent >= cls.xpRequiredInParent
    }

    fun unlockedClasses(xpPerClass: Map<String, Long>): List<HeroClass> =
        ALL.filter { isUnlocked(it.id, xpPerClass) }

    /** Ordered tier-by-tier list for the tree UI, preserving parent–child proximity. */
    fun treeOrder(): List<HeroClass> = ALL  // already defined in tree order above
}
