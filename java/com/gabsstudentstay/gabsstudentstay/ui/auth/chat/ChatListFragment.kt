package com.gabsstudentstay.gabsstudentstay.ui.auth.chat

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.*
import com.gabsstudentstay.gabsstudentstay.R
import com.gabsstudentstay.gabsstudentstay.data.db.model.ChatMessage
import com.gabsstudentstay.gabsstudentstay.utils.SessionManager
import com.gabsstudentstay.gabsstudentstay.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.*

class ChatListFragment : Fragment() {

    private val viewModel: ChatViewModel by viewModels()
    private lateinit var session: SessionManager

    private lateinit var rvChats: RecyclerView
    private lateinit var tvEmpty: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_chat_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        session  = SessionManager(requireContext())
        rvChats  = view.findViewById(R.id.rvChats)
        tvEmpty  = view.findViewById(R.id.tvEmptyChats)

        rvChats.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getConversationList(session.getUserId()).observe(viewLifecycleOwner) { messages ->
            tvEmpty.visibility = if (messages.isEmpty()) View.VISIBLE else View.GONE
            rvChats.adapter = ConversationAdapter(messages, session.getUserId()) { msg ->
                startActivity(Intent(requireContext(), ChatDetailActivity::class.java).apply {
                    putExtra("listing_id", msg.listingId)
                    // The other party is whichever end isn't the current user
                    putExtra("provider_id",
                        if (msg.senderId == session.getUserId()) msg.receiverId else msg.senderId)
                })
            }
        }
    }
}

//Conversation List Adapter
class ConversationAdapter(
    private val items: List<ChatMessage>,
    private val myId: Int,
    private val onClick: (ChatMessage) -> Unit
) : RecyclerView.Adapter<ConversationAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvPreview: TextView  = view.findViewById(R.id.tvChatPreview)
        val tvTime: TextView     = view.findViewById(R.id.tvChatTime)
        val tvListing: TextView  = view.findViewById(R.id.tvChatListingId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_conversation, parent, false)
        return VH(v)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val msg = items[position]
        val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())

        val prefix = if (msg.senderId == myId) "You: " else ""
        holder.tvPreview.text  = "$prefix${msg.message}"
        holder.tvTime.text     = sdf.format(Date(msg.timestamp))
        holder.tvListing.text  = "Listing #${msg.listingId}"
        holder.itemView.setOnClickListener { onClick(msg) }
    }
}