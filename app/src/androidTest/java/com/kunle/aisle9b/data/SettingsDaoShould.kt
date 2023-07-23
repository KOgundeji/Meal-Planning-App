package com.kunle.aisle9b.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.kunle.aisle9b.models.AppSettings
import com.kunle.aisle9b.models.SettingsEnum
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class SettingsDaoShould {
    private lateinit var sutDB: ShoppingRoomDB

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        sutDB = Room.inMemoryDatabaseBuilder(
            context, ShoppingRoomDB::class.java
        ).build()
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        sutDB.close()
    }

    @Test
    fun getAllSettings_SettingsDao() {
        runTest {
            sutDB.settingsDao().upsertSettings(AppSettings(SettingsEnum.DarkMode.name,true))
            sutDB.settingsDao().upsertSettings(AppSettings(SettingsEnum.Categories.name,true))
            sutDB.settingsDao().upsertSettings(AppSettings(SettingsEnum.ScreenPermOn.name,false))

            val listOfSettings = sutDB.settingsDao().getAllSettings().first()
            assertThat(listOfSettings.size).isEqualTo(3)
        }
    }

    @Test
    fun upsertSettings_insertNewInstance_SettingsDao() {
        runTest {
            sutDB.settingsDao().upsertSettings(AppSettings(SettingsEnum.DarkMode.name,false))

            val listOfSettings = sutDB.settingsDao().getAllSettings().first()
            assertThat(listOfSettings.size).isEqualTo(1)
        }
    }

    @Test
    fun upsertSettings_upsertSameInstance_SettingsDao() {
        runTest {
            val setting = AppSettings(SettingsEnum.DarkMode.name,false)
            sutDB.settingsDao().upsertSettings(setting)

            val listOfSettings = sutDB.settingsDao().getAllSettings().first()
            assertThat(listOfSettings.size).isEqualTo(1)

            sutDB.settingsDao().upsertSettings(setting)

            val listOfSettingsAfterInsert = sutDB.settingsDao().getAllSettings().first()
            assertThat(listOfSettingsAfterInsert.size).isEqualTo(1)
        }
    }

    @Test
    fun upsertSettings_updateInstance_SettingsDao() {
        runTest {
            val setting = AppSettings(SettingsEnum.DarkMode.name,false)
            sutDB.settingsDao().upsertSettings(setting)

            val listOfSettings = sutDB.settingsDao().getAllSettings().first()
            assertThat(listOfSettings.size).isEqualTo(1)
            assertThat(listOfSettings[0].value).isFalse()

            val updatedSetting = AppSettings(SettingsEnum.DarkMode.name,true)
            sutDB.settingsDao().upsertSettings(updatedSetting)

            val listOfSettingsAfterInsert = sutDB.settingsDao().getAllSettings().first()
            assertThat(listOfSettingsAfterInsert.size).isEqualTo(1)
            assertThat(listOfSettingsAfterInsert[0].value).isTrue()
        }
    }

    @Test
    fun deleteSettings_SettingsDao() {
        runTest {
            val setting = AppSettings(SettingsEnum.DarkMode.name,false)
            sutDB.settingsDao().upsertSettings(setting)

            val listOfSettings = sutDB.settingsDao().getAllSettings().first()
            assertThat(listOfSettings.size).isEqualTo(1)

            sutDB.settingsDao().deleteSettings(setting)

            val listOfSettingsAfterDeletion = sutDB.settingsDao().getAllSettings().first()
            assertThat(listOfSettingsAfterDeletion.size).isEqualTo(0)
        }
    }
}