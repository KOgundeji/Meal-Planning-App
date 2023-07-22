package com.kunle.aisle9b.models

import androidx.room.Entity

@Entity(primaryKeys = ["listId", "foodId"])
data class ListFoodMap(
    val listId: Long,
    val foodId: Long
)
