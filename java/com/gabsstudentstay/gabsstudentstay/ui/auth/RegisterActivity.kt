package com.gabsstudentstay.gabsstudentstay.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gabsstudentstay.gabsstudentstay.R

import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {

    private lateinit var etRegisterName: TextInputEditText
    private lateinit var etRegisterEmail: TextInputEditText
    private lateinit var etRegisterPassword: TextInputEditText
    private lateinit var btnRegister: Button
    private lateinit var tvToLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Bind exactly to the IDs in your updated activity_register.xml
        etRegisterName = findViewById(R.id.etRegisterName)
        etRegisterEmail = findViewById(R.id.etRegisterEmail)
        etRegisterPassword = findViewById(R.id.etRegisterPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvToLogin = findViewById(R.id.tvToLogin)

        btnRegister.setOnClickListener {
            performRegistration()
        }

        tvToLogin.setOnClickListener {
            // Close registration page to drop back seamlessly onto login
            finish()
        }
    }

    private fun performRegistration() {
        val name = etRegisterName.text?.toString()?.trim() ?: ""
        val email = etRegisterEmail.text?.toString()?.trim() ?: ""
        val password = etRegisterPassword.text?.toString()?.trim() ?: ""

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please complete all registration fields", Toast.LENGTH_SHORT).show()
            return
        }

        // TODO: Hook into your Room DB / API register endpoints here

        Toast.makeText(this, "Registration Successful!", Toast.LENGTH_LONG).show()
        finish()
    }
}