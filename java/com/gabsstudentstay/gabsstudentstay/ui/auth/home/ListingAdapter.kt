package com.gabsstudentstay.gabsstudentstay.ui.auth.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gabsstudentstay.gabsstudentstay.R
import com.gabsstudentstay.gabsstudentstay.data.db.model.Listing

class ListingAdapter(
    private var listings: List<Listing>,
    private val onViewClick: (Listing) -> Unit,
    private val onChatClick: (Listing) -> Unit
) : RecyclerView.Adapter<ListingAdapter.ListingViewHolder>() {

    class ListingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // FIXED: Matched directly with the true IDs inside item_listing.xml layout file
        val imgHouse: ImageView = view.findViewById(R.id.imgListing)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvLocation: TextView = view.findViewById(R.id.tvLocation)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val btnView: Button = view.findViewById(R.id.btnView)
        val btnChat: Button = view.findViewById(R.id.btnChat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_listing, parent, false)
        return ListingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListingViewHolder, position: Int) {
        val listing = listings[position]

        holder.tvTitle.text = listing.title
        holder.tvLocation.text = listing.location
        holder.tvPrice.text = "BWP ${String.format("%.0f", listing.price)}/month"

        // Map your exact Listing types ("Cottage", "Shared", "Single Room", "Bachelor Flat") to web photos
        val mainImageMap = mapOf(
            "Cottage" to "https://images.unsplash.com/photo-1568605114967-8130f3a36994?w=800",
            "Shared" to "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=800",
            "Single Room" to "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=800",
            "Bachelor Flat" to "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=800"
        )

        val normalizedType = listing.type.trim()
        val exteriorUrl = mainImageMap.entries.firstOrNull {
            it.key.equals(normalizedType, ignoreCase = true)
        }?.value ?: "https://images.unsplash.com/photo-1570129477492-45c003edd2be?w=800"

        // Dynamically pull the real web photo straight into the home list row card
        Glide.with(holder.itemView.context)
            .load(exteriorUrl)
            .placeholder(R.drawable.ic_house_placeholder)
            .error(R.drawable.ic_house_placeholder)
            .centerCrop()
            .into(holder.imgHouse)

        holder.btnView.setOnClickListener { onViewClick(listing) }
        holder.btnChat.setOnClickListener { onChatClick(listing) }
    }

    override fun getItemCount(): Int = listings.size

    fun updateData(newList: List<Listing>) {
        this.listings = newList
        notifyDataSetChanged()
    }
}