package com.gabsstudentstay.gabsstudentstay.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gabsstudentstay.gabsstudentstay.data.db.model.Reservation

@Dao
interface ReservationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservation(reservation: Reservation): Long

    @Update
    suspend fun updateReservation(reservation: Reservation)

    // All reservations for a specific student
    @Query("SELECT * FROM reservations WHERE userId = :userId ORDER BY timestamp DESC")
    fun getReservationsByUser(userId: Int): LiveData<List<Reservation>>

    // Check if a listing is already reserved (prevent double-booking)
    @Query("SELECT COUNT(*) FROM reservations WHERE listingId = :listingId AND status != 'cancelled'")
    suspend fun getActiveReservationCount(listingId: Int): Int

    // Get a reservation by reference number
    @Query("SELECT * FROM reservations WHERE referenceNumber = :ref LIMIT 1")
    suspend fun getByReferenceNumber(ref: String): Reservation?

    // Cancel a reservation
    @Query("UPDATE reservations SET status = 'cancelled' WHERE reservationId = :id")
    suspend fun cancelReservation(id: Int)

    // Confirm a reservation
    @Query("UPDATE reservations SET status = 'confirmed' WHERE reservationId = :id")
    suspend fun confirmReservation(id: Int)
}