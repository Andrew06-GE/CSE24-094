package com.gabsstudentstay.gabsstudentstay.listings

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ReferenceGenerator {

    /**
     * Generates a unique reservation reference number.
     * Format: GSS-YYYY-XXXXX  e.g. GSS-2026-04821
     */
    fun generate(): String {
        val year   = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())
        val random = (10000..99999).random()
        return "GSS-$year-$random"
    }
}