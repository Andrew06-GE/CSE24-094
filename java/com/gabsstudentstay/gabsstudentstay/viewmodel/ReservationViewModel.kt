package com.gabsstudentstay.gabsstudentstay.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.gabsstudentstay.gabsstudentstay.data.db.AppDatabase
import com.gabsstudentstay.gabsstudentstay.data.db.model.Reservation
import com.gabsstudentstay.gabsstudentstay.data.db.repository.ListingRepository
import com.gabsstudentstay.gabsstudentstay.data.db.repository.ReservationRepository
import com.gabsstudentstay.gabsstudentstay.listings.ReferenceGenerator
import kotlinx.coroutines.launch

sealed class ReservationState {
    object Idle : ReservationState()
    object Loading : ReservationState()
    data class Success(val referenceNumber: String, val depositPaid: Double) : ReservationState()
    data class Error(val message: String) : ReservationState()
}

class ReservationViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val reservationRepo = ReservationRepository(db.reservationDao())
    private val listingRepo     = ListingRepository(db.listingDao())

    private val _state = MutableLiveData<ReservationState>(ReservationState.Idle)
    val state: LiveData<ReservationState> = _state

    fun getUserReservations(userId: Int): LiveData<List<Reservation>> =
        reservationRepo.getReservationsByUser(userId)

    /**
     * Processes the deposit payment simulation:
     * 1. Checks the listing is still available (not double-booked)
     * 2. Creates a reservation record
     * 3. Marks the listing as reserved
     * 4. Returns reference number and receipt details
     */
    fun payDeposit(userId: Int, listingId: Int, depositAmount: Double) {
        viewModelScope.launch {
            _state.value = ReservationState.Loading

            // Double-booking guard
            if (reservationRepo.isListingReserved(listingId)) {
                _state.value = ReservationState.Error(
                    "Sorry, this listing was just reserved by someone else."
                )
                return@launch
            }

            val refNumber = ReferenceGenerator.generate()

            val reservation = Reservation(
                userId          = userId,
                listingId       = listingId,
                referenceNumber = refNumber,
                depositPaid     = depositAmount,
                status          = "confirmed"
            )

            val id = reservationRepo.insertReservation(reservation)
            if (id > 0) {
                // Lock the listing so no one else can reserve it
                listingRepo.markAsReserved(listingId)
                _state.value = ReservationState.Success(refNumber, depositAmount)
            } else {
                _state.value = ReservationState.Error("Payment failed. Please try again.")
            }
        }
    }

    fun resetState() { _state.value = ReservationState.Idle }
}
