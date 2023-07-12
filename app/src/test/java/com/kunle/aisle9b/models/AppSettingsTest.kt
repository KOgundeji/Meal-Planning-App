package com.kunle.aisle9b.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AppSettingsTest {

    @Test
    fun createDarkModeSettingTest() {
        val darkMode = AppSettings(settingsName = SettingsEnum.DarkMode.name, value = true)
        assertThat(darkMode).isEqualTo(AppSettings(settingsName = "DarkMode", value = true))
    }

    @Test
    fun createScreenPermOnSettingTest() {
        val screen = AppSettings(settingsName = SettingsEnum.ScreenPermOn.name, value = false)
        assertThat(screen).isEqualTo(AppSettings(settingsName = "ScreenPermOn", value = false))
    }

    @Test
    fun createCategoriesSettingTest() {
        val categories = AppSettings(settingsName = SettingsEnum.Categories.name, value = true)
        assertThat(categories).isEqualTo(AppSettings(settingsName = "Categories", value = true))
    }
}