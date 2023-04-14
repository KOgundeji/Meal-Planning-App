package com.kunle.aisle9b.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//Also contains enums related to Settings

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey
    val settingsName: String,
    @ColumnInfo
    val value: Boolean) {}

enum class AppSettings {
    DarkMode,
    ScreenPermOn,
    Categories
}

enum class Categories {
    NoCategories,
    YesCategories
}

enum class DarkMode {
    DarkModeOn,
    LightModeOn,
    DeviceDefault
}

enum class ScreenOn {
    ScreenPermOn,
    ScreenNotPermOn
}
