package com.gabsstudentstay.gabsstudentstay.ui.auth.map


import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.gabsstudentstay.gabsstudentstay.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var spinnerCampus: Spinner
    private lateinit var btnRoute: Button
    private lateinit var tvDistance: TextView

    private var googleMap: GoogleMap? = null

    // Listing location passed from ListingDetailActivity
    private var listingLat = 0.0
    private var listingLng = 0.0
    private var listingTitle = ""

    // Gaborone campuses
    private val campuses = listOf(
        Campus("University of Botswana (UB)",      -24.6579,  25.9023),
        Campus("Botho University",                  -24.6538,  25.9312),
        Campus("BIUST Palapye",                    -22.5500,  27.1333),
        Campus("Limkokwing University",             -24.6597,  25.9101),
        Campus("Botswana Accountancy College (BAC)",-24.6550,  25.9080)
    )

    data class Campus(val name: String, val lat: Double, val lng: Double)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        listingLat   = intent.getDoubleExtra("lat", -24.6541)
        listingLng   = intent.getDoubleExtra("lng", 25.9087)
        listingTitle = intent.getStringExtra("title") ?: "Listing"

        supportActionBar?.title = "View on Map"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        spinnerCampus = findViewById(R.id.spinnerCampus)
        btnRoute      = findViewById(R.id.btnShowRoute)
        tvDistance    = findViewById(R.id.tvDistance)

        mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupCampusSpinner()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Mark the listing
        val listingLatLng = LatLng(listingLat, listingLng)
        map.addMarker(
            MarkerOptions()
                .position(listingLatLng)
                .title(listingTitle)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        )

        // Mark all campuses
        campuses.forEach { campus ->
            map.addMarker(
                MarkerOptions()
                    .position(LatLng(campus.lat, campus.lng))
                    .title(campus.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )
        }

        // Centre map on listing
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(listingLatLng, 14f))
        map.uiSettings.isZoomControlsEnabled = true
    }

    private fun setupCampusSpinner() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            campuses.map { it.name }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCampus.adapter = adapter

        btnRoute.setOnClickListener { drawRoute() }
    }

    private fun drawRoute() {
        val map = googleMap ?: return
        map.clear()

        val selectedCampus = campuses[spinnerCampus.selectedItemPosition]
        val listingLatLng  = LatLng(listingLat, listingLng)
        val campusLatLng   = LatLng(selectedCampus.lat, selectedCampus.lng)

        // Re-add markers after clearing
        map.addMarker(
            MarkerOptions().position(listingLatLng).title(listingTitle)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        )
        map.addMarker(
            MarkerOptions().position(campusLatLng).title(selectedCampus.name)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        )

        // Draw straight-line route (polyline)
        // For real turn-by-turn, integrate Directions API
        map.addPolyline(
            PolylineOptions()
                .add(listingLatLng, campusLatLng)
                .width(6f)
                .color(Color.parseColor("#1565C0"))
                .geodesic(true)
        )

        // Calculate straight-line distance
        val results = FloatArray(1)
        android.location.Location.distanceBetween(
            listingLat, listingLng,
            selectedCampus.lat, selectedCampus.lng,
            results
        )
        val km = results[0] / 1000
        tvDistance.text = "Distance to ${selectedCampus.name}: ${"%.1f".format(km)} km"

        // Fit both markers in view
        val bounds = LatLngBounds.builder()
            .include(listingLatLng).include(campusLatLng).build()
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 120))
    }

    override fun onSupportNavigateUp(): Boolean { onBackPressed(); return true }
}