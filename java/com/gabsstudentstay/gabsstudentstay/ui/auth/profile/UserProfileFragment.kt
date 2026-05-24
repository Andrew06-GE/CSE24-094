package com.gabsstudentstay.gabsstudentstay.ui.auth.profile

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.gabsstudentstay.gabsstudentstay.R

class UserProfileFragment : Fragment() {

    private val CHANNEL_ID = "gabs_housing_alerts"
    private val NOTIFICATION_PERMISSION_CODE = 101

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createNotificationChannel()
        checkNotificationPermission()

        val tvProfileName = view.findViewById(R.id.tvProfileName) as? TextView
        val tvProfileEmail = view.findViewById(R.id.tvProfileEmail) as? TextView
        val spinnerArea = view.findViewById(R.id.spinnerArea) as? Spinner
        val spinnerHouseType = view.findViewById(R.id.spinnerHouseType) as? Spinner
        val etBudget = view.findViewById(R.id.etBudget) as? EditText
        val btnSavePreferences = view.findViewById(R.id.btnSavePreferences) as? Button

        // Initializing the new logout element
        val btnLogout = view.findViewById(R.id.btnLogout) as? Button

        tvProfileName?.text = "Andrew George"
        tvProfileEmail?.text = "andrew.george@student.com"

        val areas = arrayOf("Broadhurst", "Tlokweng", "Gaborone West", "Phase 2", "Maruapula", "Notwane")
        val areaAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, areas)
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerArea?.adapter = areaAdapter

        val houseTypes = arrayOf("Single Room", "Shared Room", "Bedsitter", "1 Bedroom Flat")
        val houseAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, houseTypes)
        houseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerHouseType?.adapter = houseAdapter

        val sharedPrefs = requireActivity().getSharedPreferences("StudentHousingPrefs", Context.MODE_PRIVATE)
        val savedArea = sharedPrefs.getString("PREF_AREA", "Broadhurst")
        val savedType = sharedPrefs.getString("PREF_TYPE", "Single Room")
        val savedBudget = sharedPrefs.getString("PREF_BUDGET", "")

        val areaPosition = areas.indexOf(savedArea)
        if (areaPosition >= 0) spinnerArea?.setSelection(areaPosition)

        val typePosition = houseTypes.indexOf(savedType)
        if (typePosition >= 0) spinnerHouseType?.setSelection(typePosition)

        etBudget?.setText(savedBudget)

        btnSavePreferences?.setOnClickListener {
            val selectedArea = spinnerArea?.selectedItem.toString()
            val selectedType = spinnerHouseType?.selectedItem.toString()
            val enteredBudget = etBudget?.text.toString()

            sharedPrefs.edit().apply {
                putString("PREF_AREA", selectedArea)
                putString("PREF_TYPE", selectedType)
                putString("PREF_BUDGET", enteredBudget)
                apply()
            }

            Toast.makeText(requireContext(), "Preferences saved!", Toast.LENGTH_SHORT).show()

            val budgetAmount = enteredBudget.toIntOrNull() ?: 0
            if (budgetAmount >= 1500) {
                sendLocalNotification(selectedArea, selectedType, budgetAmount)
            }
        }

        // --- LOGOUT LOGIC ACTION IMPLEMENTATION ---
        btnLogout?.setOnClickListener {
            // 1. Wipe out temporary preferences data configurations
            sharedPrefs.edit().clear().apply()

            // 2. Alert the client session state change
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()

            // 3. Close the current screen interface and return to authentication launcher context
            val activityContext = activity
            if (activityContext != null) {
                activityContext.finish()

                // If you have a specific LoginActivity.kt file un-comment the lines below to open it:
                // val intent = Intent(activityContext, LoginActivity::class.java)
                // startActivity(intent)
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Property Matching Alerts"
            val descriptionText = "Notifications when matching student rooms are found"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendLocalNotification(area: String, type: String, budget: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Gabs Student Stay Match! 🏠")
            .setContentText("A $type in $area is available within your BWP $budget budget!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(requireContext())) {
            notify(1001, builder.build())
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_CODE
                )
            }
        }
    }
}