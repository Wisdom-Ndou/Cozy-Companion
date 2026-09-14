package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ItemCategory {
    CLOTHING,
    ACCESSORY,
    FURNITURE,
    PLANT,
    RUG,
    LIGHTING,
    WALL_DECOR,
}

@Entity(tableName = "custom_items")
data class CustomItemEntity(
    @PrimaryKey
    val id: String,
    val isOwned: Boolean = false,
    val isEquipped: Boolean = false,
    val unlockedAt: Long = 0L,
)

data class CustomItem(
    val id: String,
    val name: String,
    val category: ItemCategory,
    val cost: Int,
    val description: String,
    val isOwned: Boolean = false,
    val isEquipped: Boolean = false,
)
