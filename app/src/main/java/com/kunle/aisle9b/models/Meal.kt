package com.kunle.aisle9b.models

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_table")
data class Meal(
    @PrimaryKey(autoGenerate = true)
    val mealId: Long = 0,
    val name: String,
    val servingSize: String,
    val mealPic: Uri? = null,
    val notes: String,
    val apiID: Long = -1,
    val visible: Boolean = true
) {
    constructor(name: String, servingSize: String, apiID: Int) :
            this(
                name = name,
                servingSize = servingSize,
                notes = "",
                apiID = apiID
            )

    companion object {
        fun createBlank(): Meal {
            return Meal(name = "", servingSize = "?", notes = "", visible = false)
        }
    }
}

data class MealNameUpdate(val mealId: Long, val name: String)

data class PicUpdate(val mealId: Long, val mealPic: Uri?)

data class ServingSizeUpdate(val mealId: Long, val servingSize: String)

data class NotesUpdate(val mealId: Long, val notes: String)

