package com.example.zofiajanicka.firebasechatapp

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import me.himanshusoni.chatmessageview.ChatMessageView


class ChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val dateField: TextView = itemView.findViewById(R.id.tv_date)
    private val messageField: TextView = itemView.findViewById(R.id.tv_message)
    private val chatView: ChatMessageView = itemView.findViewById(R.id.chat_view)
    private val colorSender = ContextCompat.getColor(itemView.context, R.color.material_grey_300)
    private val colorReceiver = ContextCompat.getColor(itemView.context, R.color.material_grey_800)
    private val colorPress = ContextCompat.getColor(itemView.context, R.color.material_deep_teal_200)


    fun bind(message: ChatMessage){
        setDate(message.date)
        setMessage(message.text)

        setAsSender(message.sender == FirebaseAuth.getInstance().currentUser?.uid)
    }

    fun setDate(date: String) {
        dateField.text = date
    }

    fun setMessage(message: String) {
        messageField.text = message
    }

    fun setAsSender(isSender: Boolean){
        dateField.setTextColor(colorSender)
        if (isSender) {
            messageField.setTextColor(colorReceiver)
            chatView.setBackgroundColor(colorSender, colorPress)
            chatView.gravity = Gravity.END
        } else {
            messageField.setTextColor(colorSender)
            chatView.setBackgroundColor(colorReceiver, colorPress)
            chatView.gravity = Gravity.START
        }
    }

}