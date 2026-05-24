package com.gabsstudentstay.gabsstudentstay.data.db.repository

import com.gabsstudentstay.gabsstudentstay.data.db.dao.UserDao
import com.gabsstudentstay.gabsstudentstay.data.db.model.User

class UserRepository(private val userDao: UserDao) {

    suspend fun register(user: User): Long {
        return userDao.insertUser(user)
    }

    suspend fun login(email: String, password: String): User? {
        return userDao.login(email, password)
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    suspend fun getUserById(id: Int): User? {
        return userDao.getUserById(id)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun getUserCount(): Int {
        return userDao.getUserCount()
    }

    suspend fun insertAll(users: List<User>) {
        userDao.insertAll(users)
    }
}