package com.kunle.aisle9b.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kunle.aisle9b.models.Settings
import kotlinx.coroutines.flow.Flow


@Dao
interface SettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: Settings)

    @Delete
    suspend fun deleteSettings(settings: Settings)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateSettings(settings: Settings)

    @Query("DELETE FROM settings")
    suspend fun deleteAllSettings()

    @Query("SELECT value FROM settings WHERE settingsName = :name")
    suspend fun checkSetting(name: String): Int

    @Query("SELECT * FROM settings")
    fun getAllSettings(): Flow<List<Settings>>
}