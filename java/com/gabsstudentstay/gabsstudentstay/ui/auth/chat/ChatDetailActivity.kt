package com.gabsstudentstay.gabsstudentstay.ui.auth.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gabsstudentstay.gabsstudentstay.R
import com.gabsstudentstay.gabsstudentstay.data.db.model.ChatMessage
import com.gabsstudentstay.gabsstudentstay.utils.SessionManager
import com.gabsstudentstay.gabsstudentstay.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatDetailActivity : AppCompatActivity() {

    private val viewModel: ChatViewModel by viewModels()

    private lateinit var session: SessionManager
    private lateinit var adapter: ChatMessageAdapter

    private lateinit var rvMessages: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var btnSend: ImageButton

    private var listingId = -1
    private var providerId = -1
    private var myId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_detail)

        session = SessionManager(this)

        myId = session.getUserId()
        listingId = intent.getIntExtra("listing_id", -1)
        providerId = intent.getIntExtra("provider_id", -1)

        supportActionBar?.title = "Chat with Landlord"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rvMessages = findViewById(R.id.rvMessages)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)

        adapter = ChatMessageAdapter(myId)

        rvMessages.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }

        rvMessages.adapter = adapter

        observeMessages()

        btnSend.setOnClickListener {

            val text = etMessage.text.toString().trim()

            if (text.isNotEmpty()) {
                viewModel.sendMessage(
                    myId,
                    providerId,
                    listingId,
                    text
                )

                etMessage.setText("")
            }
        }

        viewModel.markRead(myId, listingId)
    }

    private fun observeMessages() {

        viewModel.getConversation(
            listingId,
            myId,
            providerId
        ).observe(this) { messages ->

            adapter.submitList(messages)

            if (messages.isNotEmpty()) {
                rvMessages.scrollToPosition(messages.size - 1)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}

class ChatMessageAdapter(
    private val myId: Int
) : ListAdapter<ChatMessage, ChatMessageAdapter.MsgVH>(DIFF) {

    companion object {

        private const val VIEW_SENT = 1
        private const val VIEW_RECEIVED = 2

        val DIFF = object : DiffUtil.ItemCallback<ChatMessage>() {

            override fun areItemsTheSame(
                oldItem: ChatMessage,
                newItem: ChatMessage
            ): Boolean {
                return oldItem.messageId == newItem.messageId
            }

            override fun areContentsTheSame(
                oldItem: ChatMessage,
                newItem: ChatMessage
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        return if (getItem(position).senderId == myId) {
            VIEW_SENT
        } else {
            VIEW_RECEIVED
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MsgVH {

        val layoutId = if (viewType == VIEW_SENT) {
            R.layout.item_chat_sent
        } else {
            R.layout.item_chat_received
        }

        val view = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)

        return MsgVH(view)
    }

    override fun onBindViewHolder(holder: MsgVH, position: Int) {

        val message = getItem(position)

        val sdf = SimpleDateFormat(
            "HH:mm",
            Locale.getDefault()
        )

        holder.tvMsg.text = message.message
        holder.tvTime.text = sdf.format(Date(message.timestamp))
    }

    class MsgVH(view: View) : RecyclerView.ViewHolder(view) {

        val tvMsg: TextView =
            view.findViewById(R.id.tvChatMessage)

        val tvTime: TextView =
            view.findViewById(R.id.tvChatTime)
    }
}