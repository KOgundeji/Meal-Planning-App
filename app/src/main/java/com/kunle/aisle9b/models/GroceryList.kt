package com.kunle.aisle9b.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_table")
data class GroceryList(
    @PrimaryKey(autoGenerate = true)
    val listId: Long = 0,
    val listName: String,
    val visible: Boolean = true
) {
    companion object {
        fun createBlank(listId: Long = 0L): GroceryList {
            return GroceryList(listId = listId, listName = "Grocery List Name", visible = false)
        }
    }
}

data class GroceryListNameUpdate(val listId: Long, val listName: String)

data class GroceryListVisibilityUpdate(val listId: Long, val visible: Boolean = true)