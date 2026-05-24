package com.gabsstudentstay.gabsstudentstay.ui.auth.reservation

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.gabsstudentstay.gabsstudentstay.R
import com.gabsstudentstay.gabsstudentstay.utils.SessionManager
import com.gabsstudentstay.gabsstudentstay.viewmodel.ReservationState
import com.gabsstudentstay.gabsstudentstay.viewmodel.ReservationViewModel
import com.gabsstudentstay.gabsstudentstay.data.db.AppDatabase
import kotlinx.coroutines.*

class DepositActivity : AppCompatActivity() {

    private val viewModel: ReservationViewModel by viewModels()
    private lateinit var session: SessionManager

    // Payment panel
    private lateinit var layoutPayment: View
    private lateinit var tvDepositAmount: TextView
    private lateinit var etCardNumber: EditText
    private lateinit var etCardExpiry: EditText
    private lateinit var etCardCvv: EditText
    private lateinit var etCardName: EditText
    private lateinit var btnConfirmPayment: Button
    private lateinit var progressBar: ProgressBar

    // Receipt panel (shown after success)
    private lateinit var layoutReceipt: View
    private lateinit var tvReceiptRef: TextView
    private lateinit var tvReceiptAmount: TextView
    private lateinit var tvReceiptStatus: TextView
    private lateinit var btnDone: Button

    private var listingId: Int = -1
    private var depositAmount: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit)

        session       = SessionManager(this)
        listingId     = intent.getIntExtra("listing_id", -1)
        supportActionBar?.title = "Pay Deposit"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bindViews()
        loadListingDeposit()
        observeViewModel()
    }

    private fun bindViews() {
        layoutPayment      = findViewById(R.id.layoutPayment)
        tvDepositAmount    = findViewById(R.id.tvDepositAmount)
        etCardNumber       = findViewById(R.id.etCardNumber)
        etCardExpiry       = findViewById(R.id.etCardExpiry)
        etCardCvv          = findViewById(R.id.etCardCvv)
        etCardName         = findViewById(R.id.etCardName)
        btnConfirmPayment  = findViewById(R.id.btnConfirmPayment)
        progressBar        = findViewById(R.id.progressBar)

        layoutReceipt      = findViewById(R.id.layoutReceipt)
        tvReceiptRef       = findViewById(R.id.tvReceiptRef)
        tvReceiptAmount    = findViewById(R.id.tvReceiptAmount)
        tvReceiptStatus    = findViewById(R.id.tvReceiptStatus)
        btnDone            = findViewById(R.id.btnDone)

        layoutReceipt.visibility = View.GONE
    }

    private fun loadListingDeposit() {
        CoroutineScope(Dispatchers.Main).launch {
            val listing = withContext(Dispatchers.IO) {
                AppDatabase.getInstance(this@DepositActivity)
                    .listingDao().getListingById(listingId)
            }
            listing?.let {
                depositAmount = it.depositAmount
                tvDepositAmount.text =
                    "Deposit Amount: BWP ${String.format("%.2f", depositAmount)}"
            }
        }

        btnConfirmPayment.setOnClickListener {
            if (!validateCard()) return@setOnClickListener
            viewModel.payDeposit(session.getUserId(), listingId, depositAmount)
        }

        btnDone.setOnClickListener { finish() }
    }

    private fun observeViewModel() {
        viewModel.state.observe(this) { state ->
            progressBar.visibility        = if (state is ReservationState.Loading) View.VISIBLE else View.GONE
            btnConfirmPayment.isEnabled   = state !is ReservationState.Loading

            when (state) {
                is ReservationState.Success -> showReceipt(state.referenceNumber, state.depositPaid)
                is ReservationState.Error   -> Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                else -> {}
            }
        }
    }

    private fun showReceipt(refNumber: String, amount: Double) {
        layoutPayment.visibility = View.GONE
        layoutReceipt.visibility = View.VISIBLE

        tvReceiptRef.text    = "Reference: $refNumber"
        tvReceiptAmount.text = "Amount Paid: BWP ${String.format("%.2f", amount)}"
        tvReceiptStatus.text = "✅ Payment Confirmed"
    }

    private fun validateCard(): Boolean {
        val number = etCardNumber.text.toString().replace(" ", "")
        val expiry = etCardExpiry.text.toString()
        val cvv    = etCardCvv.text.toString()
        val name   = etCardName.text.toString()

        return when {
            name.isBlank()        -> err("Please enter the cardholder name.")
            number.length != 16   -> err("Card number must be 16 digits.")
            !expiry.matches(Regex("\\d{2}/\\d{2}")) -> err("Expiry format: MM/YY")
            cvv.length !in 3..4   -> err("Invalid CVV.")
            else -> true
        }
    }

    private fun err(msg: String): Boolean {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onSupportNavigateUp(): Boolean { onBackPressed(); return true }
}