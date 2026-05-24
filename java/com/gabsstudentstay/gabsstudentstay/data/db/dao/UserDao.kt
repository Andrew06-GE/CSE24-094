package com.gabsstudentstay.gabsstudentstay.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gabsstudentstay.gabsstudentstay.data.db.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(users: List<User>)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): User?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE userId = :id LIMIT 1")
    suspend fun getUserById(id: Int): User?

    @Query("SELECT * FROM users WHERE role = 'student'")
    fun getAllStudents(): LiveData<List<User>>

    @Query("SELECT * FROM users WHERE role = 'provider'")
    fun getAllProviders(): LiveData<List<User>>

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int
}