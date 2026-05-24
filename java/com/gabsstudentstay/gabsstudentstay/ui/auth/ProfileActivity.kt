package com.gabsstudentstay.gabsstudentstay.ui.auth.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.gabsstudentstay.gabsstudentstay.R
import com.gabsstudentstay.gabsstudentstay.ui.auth.LoginActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var tvProfileName: TextView
    private lateinit var tvInfoName: TextView
    private lateinit var tvInfoEmail: TextView
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar?.title = "My Profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bindProfileViews()
        loadCustomerData()

        btnLogout.setOnClickListener {
            performLogoutAction()
        }
    }

    private fun bindProfileViews() {
        tvProfileName = findViewById(R.id.tvProfileName)
        tvInfoName    = findViewById(R.id.tvInfoName)
        tvInfoEmail   = findViewById(R.id.tvInfoEmail)
        btnLogout     = findViewById(R.id.btnLogout)
    }

    private fun loadCustomerData() {
        // Safe direct preference reading bypassing custom SessionManager endpoints
        val sharedPrefs = getSharedPreferences("GabsStudentStayPrefs", Context.MODE_PRIVATE)

        // Checks both lowercase and capitalized keys to make sure it catches your registration save values
        val name = sharedPrefs.getString("name", sharedPrefs.getString("KEY_NAME", "Student User"))
        val email = sharedPrefs.getString("email", sharedPrefs.getString("KEY_EMAIL", "Registered Student"))

        tvProfileName.text = name
        tvInfoName.text    = name
        tvInfoEmail.text   = email
    }

    private fun performLogoutAction() {
        // Clear preferences storage completely
        val sharedPrefs = getSharedPreferences("GabsStudentStayPrefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().clear().apply()

        // Pop straight back onto entrance login
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}