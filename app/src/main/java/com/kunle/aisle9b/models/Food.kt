package com.kunle.aisle9b.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "food_table")
data class Food(
    @PrimaryKey
    val foodId: UUID = UUID.randomUUID(),
    @ColumnInfo
    val name: String,
    @ColumnInfo
    var quantity: String,
    @ColumnInfo
    var category: String = "",
    @ColumnInfo
    var isInGroceryList: Boolean
) {
    companion object {
        fun createBlank(): Food {
            return Food(name = "", quantity = "", isInGroceryList = false)
        }
    }
}

