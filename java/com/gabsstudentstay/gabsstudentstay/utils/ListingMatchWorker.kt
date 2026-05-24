package com.gabsstudentstay.gabsstudentstay.utils

import android.content.Context
import androidx.work.*
import com.gabsstudentstay.gabsstudentstay.data.db.AppDatabase
import java.util.concurrent.TimeUnit

/**
 * Runs periodically in the background.
 * For each user preference, checks if any new listing matches it.
 * Fires a local notification if a match is found.
 */
class ListingMatchWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val db          = AppDatabase.getInstance(context)
            val preferences = db.preferenceDao().getAllPreferences()

            preferences.forEach { pref ->
                // Find available listings that satisfy this user's preference
                val matches = db.listingDao().filterListings(
                    minPrice     = pref.minPrice,
                    maxPrice     = pref.maxPrice,
                    location     = pref.location,
                    availableFrom = pref.availableFrom,
                    type         = pref.type
                )

                // observeForever can't be used in a Worker — query synchronously instead
                // We re-use the DAO with a suspend-friendly query below
                val matchList = db.listingDao()
                    .filterListingsSync(
                        pref.minPrice, pref.maxPrice,
                        pref.location, pref.availableFrom, pref.type
                    )

                if (matchList.isNotEmpty()) {
                    val top = matchList.first()
                    NotificationHelper.sendListingMatchNotification(
                        context      = context,
                        listingTitle = top.title,
                        location     = top.location,
                        price        = top.price,
                        notificationId = pref.userId  // One notification per user
                    )
                }
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        private const val WORK_TAG = "listing_match_check"

        /**
         * Schedules a periodic check every 15 minutes.
         * Call this once from Application.onCreate() or after a preference is saved.
         */
        fun schedule(context: Context) {
            val request = PeriodicWorkRequestBuilder<ListingMatchWorker>(
                15, TimeUnit.MINUTES
            )
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                        .build()
                )
                .addTag(WORK_TAG)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelAllWorkByTag(WORK_TAG)
        }
    }
}