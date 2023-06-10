package com.kunle.aisle9b.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kunle.aisle9b.navigation.GroceryScreens

@Entity(tableName = "category_table")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val categoryEntityID: Int = 0,
    @ColumnInfo
    val foodName: String,
    @ColumnInfo
    val categoryName: String
) {
    companion object Static {
        val categories = arrayOf(
            "Baking/Spices",
            "Beverages",
            "Bread/Grain",
            "Canned Goods",
            "Condiments",
            "Dairy",
            "For the Home",
            "Frozen Food",
            "Fruit",
            "Meat/Fish",
            "Pet Supplies",
            "Produce",
            "Snacks",
            "Toiletries",
            "Uncategorized"
        )
    }

    fun createNew(): Category {
        return Category(foodName = "", categoryName = "Uncategorized")
    }
}

