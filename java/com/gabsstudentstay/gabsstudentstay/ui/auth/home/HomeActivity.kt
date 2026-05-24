package com.gabsstudentstay.gabsstudentstay.ui.auth.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gabsstudentstay.gabsstudentstay.R
import com.gabsstudentstay.gabsstudentstay.ui.auth.LoginActivity
import com.gabsstudentstay.gabsstudentstay.ui.auth.chat.ChatListFragment
import com.gabsstudentstay.gabsstudentstay.ui.auth.reservation.MyReservationsFragment
import com.gabsstudentstay.gabsstudentstay.utils.ListingMatchWorker
import com.gabsstudentstay.gabsstudentstay.utils.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gabsstudentstay.gabsstudentstay.ui.auth.profile.UserProfileFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var session: SessionManager
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        session = SessionManager(this)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        ListingMatchWorker.schedule(this)

        setupBottomNav()

        if (savedInstanceState == null) {
            loadFragment(ListingsFragment())
        }
    }

    private fun setupBottomNav() {
        bottomNavigation.setOnItemSelectedListener { item ->
            val targetFragment: Fragment = when (item.itemId) {
                R.id.nav_listings -> ListingsFragment()
                R.id.nav_reservations -> MyReservationsFragment()
                R.id.nav_chat -> ChatListFragment()
                R.id.nav_profile -> UserProfileFragment()
                else -> return@setOnItemSelectedListener false
            }
            loadFragment(targetFragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logoutUser()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Extracted out so your profile fragment logout can easily point here if needed
    fun logoutUser() {
        ListingMatchWorker.cancel(this)
        session.clearSession()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}