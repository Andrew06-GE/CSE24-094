package com.gabsstudentstay.gabsstudentstay.ui.auth.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gabsstudentstay.gabsstudentstay.R
import com.gabsstudentstay.gabsstudentstay.ui.auth.chat.ChatDetailActivity
import com.gabsstudentstay.gabsstudentstay.ui.auth.map.MapActivity
import com.gabsstudentstay.gabsstudentstay.ui.auth.reservation.DepositActivity
import com.gabsstudentstay.gabsstudentstay.viewmodel.ListingViewModel

class ListingDetailActivity : AppCompatActivity() {

    private val viewModel: ListingViewModel by viewModels()

    private lateinit var tvTitle: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvType: TextView
    private lateinit var tvAmenities: TextView
    private lateinit var tvAvailable: TextView
    private lateinit var tvDeposit: TextView
    private lateinit var tvDescription: TextView
    private lateinit var imgListing: ImageView
    private lateinit var tvReservedBadge: TextView
    private lateinit var btnDeposit: Button
    private lateinit var btnViewMap: Button
    private lateinit var btnChat: Button
    private lateinit var rvThumbnails: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bindViews()

        val listingId = intent.getIntExtra("listing_id", -1)
        if (listingId != -1) viewModel.loadListingById(listingId)

        observeViewModel()
    }

    private fun bindViews() {
        tvTitle         = findViewById(R.id.tvDetailTitle)
        tvLocation      = findViewById(R.id.tvDetailLocation)
        tvPrice         = findViewById(R.id.tvDetailPrice)
        tvType          = findViewById(R.id.tvDetailType)
        tvAmenities     = findViewById(R.id.tvDetailAmenities)
        tvAvailable     = findViewById(R.id.tvDetailAvailable)
        tvDeposit       = findViewById(R.id.tvDetailDeposit)
        tvDescription   = findViewById(R.id.tvDetailDescription)
        imgListing      = findViewById(R.id.imgDetailListing)
        tvReservedBadge = findViewById(R.id.tvDetailReservedBadge)
        btnDeposit      = findViewById(R.id.btnPayDeposit)
        btnViewMap      = findViewById(R.id.btnViewMap)
        btnChat         = findViewById(R.id.btnChat)
        rvThumbnails    = findViewById(R.id.rvThumbnails)
    }

    private fun observeViewModel() {
        viewModel.selectedListing.observe(this) { listing ->
            listing ?: return@observe

            supportActionBar?.title = listing.title

            tvTitle.text       = listing.title
            tvLocation.text    = "Location: ${listing.location}"
            tvPrice.text       = "BWP ${String.format("%.0f", listing.price)} / month"
            tvType.text        = "Type: ${listing.type}"
            tvAmenities.text   = listing.amenities.split(",").joinToString("  .  ") { "Property Feature: $it" }
            tvAvailable.text   = "Available from: ${listing.availabilityDate}"
            tvDeposit.text     = "Deposit: BWP ${String.format("%.0f", listing.depositAmount)}"
            tvDescription.text = listing.description.ifBlank { "No description provided." }

            // Dynamic Image Array Setup mapped to structural accommodation types
            val imageMap = mapOf(
                "Cottage" to listOf(
                    "https://images.unsplash.com/photo-1568605114967-8130f3a36994?w=800",
                    "https://images.unsplash.com/photo-1556911220-e15b29be8c8f?w=800",
                    "https://images.unsplash.com/photo-1616594039964-ae9021a400a0?w=800"
                ),
                "Shared" to listOf(
                    "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=800",
                    "https://images.unsplash.com/photo-1595526114035-0d45ed16cfbf?w=800",
                    "https://images.unsplash.com/photo-1505691938895-1758d7feb511?w=800"
                ),
                "Single Room" to listOf(
                    "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=800",
                    "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?w=800",
                    "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=800"
                ),
                "Bachelor Flat" to listOf(
                    "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=800",
                    "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=800",
                    "https://images.unsplash.com/photo-1484154218962-a197022b5858?w=800"
                )
            )

            val normalizedType = listing.type.trim()
            val imageUrls = imageMap.entries.firstOrNull {
                it.key.equals(normalizedType, ignoreCase = true)
            }?.value ?: listOf(
                "https://images.unsplash.com/photo-1570129477492-45c003edd2be?w=800"
            )

            // Load primary image safely using Glide
            Glide.with(this)
                .load(imageUrls[0])
                .placeholder(R.drawable.ic_house_placeholder)
                .error(R.drawable.ic_house_placeholder)
                .centerCrop()
                .into(imgListing)

            // Setup scrolling gallery thumbnail list view
            val thumbnailAdapter = ThumbnailUrlAdapter(imageUrls) { clickedUrl ->
                Glide.with(this)
                    .load(clickedUrl)
                    .placeholder(R.drawable.ic_house_placeholder)
                    .centerCrop()
                    .into(imgListing)
            }
            rvThumbnails.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            rvThumbnails.adapter = thumbnailAdapter

            if (listing.isReserved) {
                tvReservedBadge.visibility = View.VISIBLE
                btnDeposit.isEnabled       = false
                btnDeposit.text            = "Already Reserved"
            } else {
                tvReservedBadge.visibility = View.GONE
                btnDeposit.isEnabled       = true
                btnDeposit.text            = "Pay Deposit"
            }

            btnDeposit.setOnClickListener {
                startActivity(Intent(this, DepositActivity::class.java).apply {
                    putExtra("listing_id", listing.listingId)
                })
            }

            btnViewMap.setOnClickListener {
                startActivity(Intent(this, MapActivity::class.java).apply {
                    putExtra("lat", listing.latitude)
                    putExtra("lng", listing.longitude)
                    putExtra("title", listing.title)
                })
            }

            btnChat.setOnClickListener {
                startActivity(Intent(this, ChatDetailActivity::class.java).apply {
                    putExtra("listing_id", listing.listingId)
                    putExtra("provider_id", listing.providerId)
                })
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}