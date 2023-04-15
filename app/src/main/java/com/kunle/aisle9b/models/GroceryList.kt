package com.kunle.aisle9b.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "list_table")
data class GroceryList(
@PrimaryKey
val listId: UUID = UUID.randomUUID(),
@ColumnInfo
val name: String) {}