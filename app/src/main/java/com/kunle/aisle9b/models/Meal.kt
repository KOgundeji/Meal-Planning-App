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
    val mealPic: Uri? = Uri.EMPTY,
    val notes: String,
    val apiID: Int = -1,
    val apiImageURL: String? = null,
    val visible: Boolean = true
) {
    constructor(name: String, servingSize: String, apiID: Int, apiImageURL: String?) :
            this(
                name = name,
                servingSize = servingSize,
                notes = "",
                apiID = apiID,
                apiImageURL = apiImageURL
            )

    constructor(
        mealId: Long,
        name: String,
        servingSize: String,
        mealPic: Uri,
        notes: String,
        visible: Boolean
    ) :
            this(
                mealId = mealId,
                name = name,
                servingSize = servingSize,
                mealPic = mealPic,
                notes = notes,
                apiID = -1,
                visible = visible
            )

    constructor(mealId: Long, name: String, servingSize: String, apiID: Int, apiImageURL: String?) :
            this(
                mealId = mealId,
                name = name,
                servingSize = servingSize,
                notes = "",
                apiID = apiID,
                apiImageURL = apiImageURL
            )

    companion object {
        fun createBlank(mealId: Long = 0L): Meal {
            return Meal(
                mealId = mealId,
                name = "Name of Meal",
                servingSize = "?",
                notes = "",
                visible = false
            )
        }

        fun makeNewMealVisible(meal: Meal): Meal {
            return meal.copy(visible = true)
        }
    }
}

data class MealNameUpdate(val mealId: Long, val name: String)

data class MealPicUpdate(val mealId: Long, val mealPic: Uri)

data class MealServingSizeUpdate(val mealId: Long, val servingSize: String)

data class MealNotesUpdate(val mealId: Long, val notes: String)

data class MealVisibilityUpdate(val mealId: Long, val visible: Boolean)

