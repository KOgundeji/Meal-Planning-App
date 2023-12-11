package com.kunle.aisle9b.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_table")
data class Food(
    @PrimaryKey(autoGenerate = true)
    val foodId: Long = 0L,
    val name: String,
    val quantity: String,
    val category: String = "Uncategorized"
) {
    companion object {
        fun createBlank(): Food {
            return Food(name = "", quantity = "")
        }

        val categories = arrayOf(
            "Uncategorized",
            "Beverages",
            "Breads and Grains",
            "Canned/Jarred Goods",
            "Condiments & Spices",
            "Dairy/Eggs",
            "Frozen Foods",
            "Household & Cleaning Supplies",
            "Meat/Poultry/Fish",
            "Personal Care",
            "Produce (Fruits & Veggies)",
            "Snacks",
            "Other"
        )
    }

    fun foodToGrocery(): Grocery {
        return Grocery(
            name = this.name,
            quantity = this.quantity,
            category = this.category
        )
    }
}

