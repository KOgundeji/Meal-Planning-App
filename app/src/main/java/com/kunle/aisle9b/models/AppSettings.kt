package com.kunle.aisle9b.models

import androidx.room.Entity
import androidx.room.PrimaryKey

//Also contains enums related to Settings

@Entity(tableName = "settings")
data class AppSettings(
    @PrimaryKey val settingsName: String,
    val value: Boolean
)

enum class SettingsEnum {
    DarkMode,
    ScreenPermOn,
    Categories,
    ViewMealsAsLists
}

