package com.gabsstudentstay.gabsstudentstay.ui.auth.reservation

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gabsstudentstay.gabsstudentstay.R
import com.gabsstudentstay.gabsstudentstay.data.db.model.Reservation
import com.gabsstudentstay.gabsstudentstay.utils.SessionManager
import com.gabsstudentstay.gabsstudentstay.viewmodel.ReservationViewModel
import java.text.SimpleDateFormat
import java.util.*

class MyReservationsFragment : Fragment() {

    private val viewModel: ReservationViewModel by viewModels()
    private lateinit var session: SessionManager

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmpty: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_my_reservations, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        session      = SessionManager(requireContext())
        recyclerView = view.findViewById(R.id.rvReservations)
        tvEmpty      = view.findViewById(R.id.tvEmptyReservations)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getUserReservations(session.getUserId()).observe(viewLifecycleOwner) { reservations ->
            tvEmpty.visibility = if (reservations.isEmpty()) View.VISIBLE else View.GONE
            recyclerView.adapter = ReservationAdapter(reservations)
        }
    }
}

// ── Inline adapter (simple enough to keep here) ───────────────────────────────
class ReservationAdapter(
    private val items: List<Reservation>
) : RecyclerView.Adapter<ReservationAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvRef: TextView    = view.findViewById(R.id.tvResRef)
        val tvAmount: TextView = view.findViewById(R.id.tvResAmount)
        val tvStatus: TextView = view.findViewById(R.id.tvResStatus)
        val tvDate: TextView   = view.findViewById(R.id.tvResDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reservation, parent, false)
        return VH(v)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val res = items[position]
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

        holder.tvRef.text    = "Ref: ${res.referenceNumber}"
        holder.tvAmount.text = "BWP ${String.format("%.2f", res.depositPaid)}"
        holder.tvStatus.text = res.status.replaceFirstChar { it.uppercase() }
        holder.tvDate.text   = sdf.format(Date(res.timestamp))

        // Colour-code status
        val ctx = holder.itemView.context
        holder.tvStatus.setTextColor(
            when (res.status) {
                "confirmed"  -> ctx.getColor(R.color.green)
                "cancelled"  -> ctx.getColor(R.color.red)
                else         -> ctx.getColor(R.color.orange)
            }
        )
    }
}