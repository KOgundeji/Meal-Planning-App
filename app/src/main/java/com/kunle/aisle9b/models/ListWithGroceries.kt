package com.kunle.aisle9b.models

import androidx.room.*

@Entity()
data class ListWithGroceries(
    @Embedded
    val list: GroceryList,
    @Relation(
        parentColumn = "listId",
        entityColumn = "foodId",
        associateBy = Junction(ListFoodMap::class)
    )
    val groceries: List<Food>
)
