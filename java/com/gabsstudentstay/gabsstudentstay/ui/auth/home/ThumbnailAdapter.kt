package com.gabsstudentstay.gabsstudentstay.ui.auth.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gabsstudentstay.gabsstudentstay.R

class ThumbnailUrlAdapter(
    private val images: List<String>,
    private val onImageClick: (String) -> Unit
) : RecyclerView.Adapter<ThumbnailUrlAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imgThumbnail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Creates an image view dynamically or inflates your row item
        val view = ImageView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(120, ViewGroup.LayoutParams.MATCH_PARENT)
            scaleType = ImageView.ScaleType.CENTER_CROP
            setPadding(4, 4, 4, 4)
            id = R.id.imgThumbnail
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val url = images[position]

        Glide.with(holder.itemView.context)
            .load(url)
            .placeholder(R.drawable.ic_house_placeholder)
            .centerCrop()
            .into(holder.imageView)

        holder.itemView.setOnClickListener { onImageClick(url) }
    }

    override fun getItemCount(): Int = images.size
}