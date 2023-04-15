package com.kunle.aisle9b.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

@Entity
data class ListWithGroceries(
    @Embedded
    val list: GroceryList,
    @Relation(
        parentColumn = "listId",
        entityColumn = "foodId",
        associateBy = Junction(ListFoodMap::class)
    ) val groceries: List<Food>
)
