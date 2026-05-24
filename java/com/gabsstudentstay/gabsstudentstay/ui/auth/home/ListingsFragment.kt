package com.gabsstudentstay.gabsstudentstay.ui.auth.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gabsstudentstay.gabsstudentstay.R
import com.gabsstudentstay.gabsstudentstay.ui.auth.chat.ChatDetailActivity
import com.gabsstudentstay.gabsstudentstay.viewmodel.ListingViewModel

class ListingsFragment : Fragment() {

    private val viewModel: ListingViewModel by viewModels()
    private lateinit var listingAdapter: ListingAdapter
    private lateinit var rvListings: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var tvEmptyListings: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_listings, container, false)

        rvListings = view.findViewById(R.id.rvListings)
        searchView = view.findViewById(R.id.searchView)
        tvEmptyListings = view.findViewById(R.id.tvEmptyListings)

        setupRecyclerView()
        setupSearchView()
        observeViewModel()

        return view
    }

    private fun setupRecyclerView() {
        listingAdapter = ListingAdapter(
            listings = emptyList(),
            onViewClick = { listing ->
                val intent = Intent(requireContext(), ListingDetailActivity::class.java).apply {
                    putExtra("listing_id", listing.listingId)
                }
                startActivity(intent)
            },
            onChatClick = { listing ->
                val intent = Intent(requireContext(), ChatDetailActivity::class.java).apply {
                    putExtra("listing_id", listing.listingId)
                    putExtra("provider_id", listing.providerId)
                }
                startActivity(intent)
            }
        )

        rvListings.layoutManager = LinearLayoutManager(requireContext())
        rvListings.adapter = listingAdapter
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.search(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.search(newText.orEmpty())
                return true
            }
        })
    }

    private fun observeViewModel() {
        viewModel.filteredListings.observe(viewLifecycleOwner) { listingsList ->
            if (listingsList != null) {
                listingAdapter.updateData(listingsList)

                if (listingsList.isEmpty()) {
                    tvEmptyListings.visibility = View.VISIBLE
                    rvListings.visibility = View.GONE
                } else {
                    tvEmptyListings.visibility = View.GONE
                    rvListings.visibility = View.VISIBLE
                }
            }
        }
    }
}