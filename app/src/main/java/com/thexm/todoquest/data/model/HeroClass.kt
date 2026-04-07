package com.thexm.todoquest.data.model

data class HeroClass(
    val id: String,
    val displayName: String,
    val description: String,       // flavour description shown in the tree
    val emoji: String,
    val tier: Int,                 // 0 = root, 1 = journeyman, 2 = specialist, 3 = master
    val parentId: String?,         // null only for the root class
    val xpRequiredInParent: Long   // XP that must be earned WHILE parent is active
)
