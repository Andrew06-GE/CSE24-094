package com.gabsstudentstay.gabsstudentstay.ui.auth.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import com.gabsstudentstay.gabsstudentstay.R
import com.gabsstudentstay.gabsstudentstay.viewmodel.ListingViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.slider.RangeSlider

class FilterBottomSheet : BottomSheetDialogFragment() {

    private val viewModel: ListingViewModel by activityViewModels()

    private lateinit var priceRangeSlider: RangeSlider
    private lateinit var tvPriceRange: TextView
    private lateinit var etLocation: EditText
    private lateinit var etAvailableFrom: EditText
    private lateinit var spinnerType: Spinner
    private lateinit var btnApply: Button
    private lateinit var btnClear: Button

    private val roomTypes = listOf("Any", "Single Room", "Bachelor Flat", "Shared", "Cottage")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.bottom_sheet_filter, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        priceRangeSlider = view.findViewById(R.id.priceRangeSlider)
        tvPriceRange     = view.findViewById(R.id.tvPriceRange)
        etLocation       = view.findViewById(R.id.etFilterLocation)
        etAvailableFrom  = view.findViewById(R.id.etFilterAvailableFrom)
        spinnerType      = view.findViewById(R.id.spinnerRoomType)
        btnApply         = view.findViewById(R.id.btnApplyFilter)
        btnClear         = view.findViewById(R.id.btnClearFilter)

        setupPriceSlider()
        setupTypeSpinner()
        setupButtons()
    }

    private fun setupPriceSlider() {
        priceRangeSlider.valueFrom = 0f
        priceRangeSlider.valueTo   = 10000f
        priceRangeSlider.values    = listOf(0f, 5000f)
        priceRangeSlider.stepSize  = 100f

        updatePriceLabel(0f, 5000f)

        priceRangeSlider.addOnChangeListener { slider, _, _ ->
            val values = slider.values
            updatePriceLabel(values[0], values[1])
        }
    }

    private fun updatePriceLabel(min: Float, max: Float) {
        tvPriceRange.text = "BWP ${min.toInt()} – BWP ${max.toInt()}"
    }

    private fun setupTypeSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            roomTypes
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerType.adapter = adapter
    }

    private fun setupButtons() {
        btnApply.setOnClickListener {
            val values    = priceRangeSlider.values
            val minPrice  = values[0].toDouble()
            val maxPrice  = values[1].toDouble()
            val location  = etLocation.text.toString().trim()
            val available = etAvailableFrom.text.toString().trim()
            val type      = if (spinnerType.selectedItemPosition == 0) ""
            else roomTypes[spinnerType.selectedItemPosition]

            viewModel.applyFilter(minPrice, maxPrice, location, available, type)
            dismiss()
        }

        btnClear.setOnClickListener {
            viewModel.clearFilter()
            dismiss()
        }
    }
}