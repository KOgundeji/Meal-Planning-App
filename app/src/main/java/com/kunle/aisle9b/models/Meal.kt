package com.kunle.aisle9b.models

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "meal_table")
open class Meal(
    @PrimaryKey
    val mealId: UUID = UUID.randomUUID(),
    @ColumnInfo
    var name: String,
    @ColumnInfo
    var servingSize: String,
    @ColumnInfo
    var mealPic: Uri? = null,
    @ColumnInfo
    var notes: String,
    @ColumnInfo
    var api: Boolean = false,
    @ColumnInfo
    val apiID: Int? = null,

) {
    constructor(name: String, apiID: Int) : this(name = name, apiID = apiID, servingSize = "", notes = "", api = true)

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

