package com.kunle.aisle9b.data

import androidx.room.*
import com.kunle.aisle9b.models.AppSettings
import kotlinx.coroutines.flow.Flow


@Dao
interface SettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: AppSettings)

    @Delete
    suspend fun deleteSettings(settings: AppSettings)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateSettings(settings: AppSettings)

    @Query("DELETE FROM settings")
    suspend fun deleteAllSettings()

    @Query("SELECT value FROM settings WHERE settingsName = :name")
    suspend fun checkSetting(name: String): Int

    @Query("SELECT * FROM settings")
    fun getAllSettings(): Flow<List<AppSettings>>
}