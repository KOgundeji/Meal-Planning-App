package com.kunle.aisle9b.data

import androidx.room.*
import com.kunle.aisle9b.models.AppSettings
import kotlinx.coroutines.flow.Flow


@Dao
interface SettingsDao {
    @Upsert
    suspend fun upsertSettings(settings: AppSettings)

    @Delete
    suspend fun deleteSettings(settings: AppSettings)

    @Query("SELECT * FROM settings")
    fun getAllSettings(): Flow<List<AppSettings>>
}