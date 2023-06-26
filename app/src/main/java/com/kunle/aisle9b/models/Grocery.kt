package com.kunle.aisle9b.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grocery_table")
open class Grocery(
    val groceryId: Long = 0,
    name: String,
    quantity: String,
    category: String
) : Food(name = name, quantity = quantity, category = category)
