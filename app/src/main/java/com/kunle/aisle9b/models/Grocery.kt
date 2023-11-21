package com.kunle.aisle9b.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grocery_table")
data class Grocery(
    @PrimaryKey(autoGenerate = true)
    val groceryId: Long = 0,
    val name: String,
    val quantity: String,
    val category: String = "Uncategorized"
) {
    fun groceryToFood(): Food {
        return Food(
            name = this.name,
            quantity = this.quantity,
            category = this.category
        )
    }
}
