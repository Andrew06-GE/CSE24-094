package com.gabsstudentstay.gabsstudentstay.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gabsstudentstay.gabsstudentstay.data.db.AppDatabase
import com.gabsstudentstay.gabsstudentstay.data.db.model.User
import com.gabsstudentstay.gabsstudentstay.data.db.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(
        AppDatabase.getInstance(application).userDao()
    )

    private val _authState = MutableLiveData<AuthState>(AuthState.Idle)
    val authState: LiveData<AuthState> = _authState

    // ── REGISTER ──────────────────────────────────────────────────────────────
    fun register(name: String, email: String, password: String,
                 studentId: String, phone: String) {

        if (!validateRegistration(name, email, password, studentId)) return

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            // Shift database reading/writing operations entirely to the IO thread pool
            val authResult = withContext(Dispatchers.IO) {
                val existing = repository.getUserByEmail(email)
                if (existing != null) {
                    return@withContext AuthState.Error("An account with this email already exists.")
                }

                val newUser = User(
                    name      = name.trim(),
                    email     = email.trim().lowercase(),
                    password  = password,
                    role      = "student",
                    studentId = studentId.trim(),
                    phone     = phone.trim()
                )

                val id = repository.register(newUser)
                if (id > 0) {
                    val createdUser = repository.getUserById(id.toInt())
                    if (createdUser != null) {
                        AuthState.Success(createdUser)
                    } else {
                        AuthState.Error("Registration failed. Please try again.")
                    }
                } else {
                    AuthState.Error("Registration failed. Please try again.")
                }
            }

            // Return result back cleanly to the main UI Thread
            _authState.value = authResult
        }
    }

    // ── LOGIN ─────────────────────────────────────────────────────────────────
    fun login(email: String, password: String) {

        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Please enter your email and password.")
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            // Safe asynchronous database call execution block
            val loginResult = withContext(Dispatchers.IO) {
                val user = repository.login(email.trim().lowercase(), password)
                if (user != null) {
                    AuthState.Success(user)
                } else {
                    AuthState.Error("Invalid email or password.")
                }
            }

            _authState.value = loginResult
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }

    // ── VALIDATION ────────────────────────────────────────────────────────────
    private fun validateRegistration(
        name: String, email: String, password: String, studentId: String
    ): Boolean {
        return when {
            name.isBlank()      -> { _authState.value = AuthState.Error("Name is required."); false }
            email.isBlank()     -> { _authState.value = AuthState.Error("Email is required."); false }
            !email.contains("@") -> { _authState.value = AuthState.Error("Enter a valid email address."); false }
            password.length < 6 -> { _authState.value = AuthState.Error("Password must be at least 6 characters."); false }
            studentId.isBlank() -> { _authState.value = AuthState.Error("Student ID is required."); false }
            else                -> true
        }
    }
}