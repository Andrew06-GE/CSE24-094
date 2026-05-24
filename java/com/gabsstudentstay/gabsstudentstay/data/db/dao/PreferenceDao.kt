package com.gabsstudentstay.gabsstudentstay.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gabsstudentstay.gabsstudentstay.data.db.model.Preference

@Dao
interface PreferenceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreference(preference: Preference): Long

    @Update
    suspend fun updatePreference(preference: Preference)

    // One preference record per user
    @Query("SELECT * FROM preferences WHERE userId = :userId LIMIT 1")
    suspend fun getPreferenceByUser(userId: Int): Preference?

    @Query("SELECT * FROM preferences WHERE userId = :userId LIMIT 1")
    fun observePreference(userId: Int): LiveData<Preference?>

    // Get all preferences (used by WorkManager to match new listings)
    @Query("SELECT * FROM preferences")
    suspend fun getAllPreferences(): List<Preference>

    @Query("DELETE FROM preferences WHERE userId = :userId")
    suspend fun deletePreference(userId: Int)
}