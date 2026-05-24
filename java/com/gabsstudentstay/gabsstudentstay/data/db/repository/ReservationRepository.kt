package com.gabsstudentstay.gabsstudentstay.data.db.repository

import androidx.lifecycle.LiveData
import com.gabsstudentstay.gabsstudentstay.data.db.dao.ReservationDao
import com.gabsstudentstay.gabsstudentstay.data.db.model.Reservation

class ReservationRepository(private val reservationDao: ReservationDao) {

    suspend fun insertReservation(reservation: Reservation): Long =
        reservationDao.insertReservation(reservation)

    fun getReservationsByUser(userId: Int): LiveData<List<Reservation>> =
        reservationDao.getReservationsByUser(userId)

    suspend fun isListingReserved(listingId: Int): Boolean =
        reservationDao.getActiveReservationCount(listingId) > 0

    suspend fun cancelReservation(reservationId: Int) =
        reservationDao.cancelReservation(reservationId)

    suspend fun confirmReservation(reservationId: Int) =
        reservationDao.confirmReservation(reservationId)
}