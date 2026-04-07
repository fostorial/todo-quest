package com.thexm.todoquest.data.model

data class Title(
    val id: String,
    val displayName: String,
    val xpTier: XPTier?,       // null = default title (no rarity)
    val levelRequired: Int,
    val flavorText: String
) {
    val isDefault: Boolean get() = xpTier == null
}
