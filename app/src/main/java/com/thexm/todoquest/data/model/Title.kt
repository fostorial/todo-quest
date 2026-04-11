package com.thexm.todoquest.data.model

data class Title(
    val id: String,
    val displayName: String,
    val xpTier: XPTier?,       // null = default title (no rarity)
    val levelRequired: Int,
    val flavorText: String,
    val classRequired: String? = null  // if set, unlocks when this hero class is unlocked
) {
    val isDefault: Boolean get() = xpTier == null && classRequired == null
}
