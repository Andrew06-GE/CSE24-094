package com.gabsstudentstay.gabsstudentstay

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.gabsstudentstay.gabsstudentstay.data.db.AppDatabase
import com.gabsstudentstay.gabsstudentstay.data.db.repository.ListingRepository
import com.gabsstudentstay.gabsstudentstay.data.db.repository.UserRepository
import com.gabsstudentstay.gabsstudentstay.ui.auth.LoginActivity
import com.gabsstudentstay.gabsstudentstay.ui.auth.home.HomeActivity
import com.gabsstudentstay.gabsstudentstay.utils.DataSeeder
import com.gabsstudentstay.gabsstudentstay.utils.NotificationHelper
import com.gabsstudentstay.gabsstudentstay.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        val session = SessionManager(this)

        lifecycleScope.launch {

            if (!session.isSeeded()) {
                withContext(Dispatchers.IO) {
                    val db = AppDatabase.getInstance(this@SplashActivity)
                    val userRepo = UserRepository(db.userDao())
                    val listingRepo = ListingRepository(db.listingDao())
                    DataSeeder.seed(userRepo, listingRepo)
                }
                session.markSeeded()
            }

            NotificationHelper.createNotificationChannel(this@SplashActivity)

            delay(1500)

            val destination =
                if (session.isLoggedIn()) HomeActivity::class.java
                else LoginActivity::class.java

            startActivity(Intent(this@SplashActivity, destination))
            finish()
        }
    }
}