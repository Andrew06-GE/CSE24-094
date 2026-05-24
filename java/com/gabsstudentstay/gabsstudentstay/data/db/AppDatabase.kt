package com.gabsstudentstay.gabsstudentstay.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gabsstudentstay.gabsstudentstay.data.db.dao.*
import com.gabsstudentstay.gabsstudentstay.data.db.model.*

@Database(
    entities = [
        User::class,
        Listing::class,
        Reservation::class,
        ChatMessage::class,
        Preference::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun listingDao(): ListingDao
    abstract fun reservationDao(): ReservationDao
    abstract fun chatDao(): ChatDao
    abstract fun preferenceDao(): PreferenceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gabsstudentstay_db"
                )
                    .createFromAsset("gabsstudentstay_db.db")
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}