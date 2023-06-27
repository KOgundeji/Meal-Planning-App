package com.kunle.aisle9b.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_table")
class Food(
    @PrimaryKey(autoGenerate = true)
    var foodId: Long = 0L,
    val name: String,
    val quantity: String,
    val category: String = "Uncategorized"
) {
    companion object {
        fun createBlank(): Food {
            return Food(name = "", quantity = "")
        }

        fun foodToGrocery(food: Food): Grocery {
            return Grocery(
                name = food.name,
                quantity = food.quantity,
                category = food.category
            )
        }

        val categories = arrayOf(
            "Uncategorized",
            "Baby Items",
            "Baking",
            "Beverages",
            "Bread & Bakery",
            "Canned Goods",
            "Condiments & Spices",
            "Dairy",
            "Deli",
            "Fish & Seafood",
            "Frozen Foods",
            "Fruits",
            "Health Care",
            "Household & Cleaning Supplies",
            "Meat",
            "Pasta, Rice & Cereal",
            "Personal Care",
            "Pet Care",
            "Snacks",
            "Vegetables"
        )
    }
}

