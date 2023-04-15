package com.kunle.aisle9b.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.*

@Entity(primaryKeys = ["listId","foodId"])
data class ListFoodMap(
@ColumnInfo(index = true)
val listId: UUID,
val foodId: UUID
) {
}
