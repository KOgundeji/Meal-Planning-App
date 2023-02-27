package com.kunle.aisle9b.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "meal_table")
data class Meal(
    @PrimaryKey
    val mealId: UUID = UUID.randomUUID(),
    @ColumnInfo
    val name: String) {}
