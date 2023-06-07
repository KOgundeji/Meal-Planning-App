package com.kunle.aisle9b.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import android.net.Uri
import java.util.*

@Entity(tableName = "meal_table")
open class Meal(
    @PrimaryKey
    val mealId: UUID = UUID.randomUUID(),
    @ColumnInfo
    val name: String,
    @ColumnInfo
    val servingSize: String,
    @ColumnInfo
    val mealPic: Uri? = null,
    @ColumnInfo
    val notes: String,
    @ColumnInfo
    val apiID: Int? = null
) {
    constructor(name: String, apiID: Int) : this(name = name, apiID = apiID, servingSize = "", notes = "")

    companion object {
        fun createBlank(): Meal {
            return Meal(name = "", servingSize = "?", notes = "")
        }
    }
}

data class MealNameUpdate(val mealId: UUID, val name: String)

data class PicUpdate(val mealId: UUID, val mealPic: Uri?)

data class ServingSizeUpdate(val mealId: UUID, val servingSize: String)

data class NotesUpdate(val mealId: UUID, val notes: String)

