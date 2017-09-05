package com.example.zofiajanicka.firebasechatapp

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView



class ChatAdapter(context: Context, resource: Int, objects: List<ChatMessage>) : ArrayAdapter<ChatMessage>(context, resource, objects) {

    override fun getView(position: Int, view: View, parent: ViewGroup): View {
        var view = view
        if (view == null) {
            view = (context as Activity).layoutInflater.inflate(R.layout.item_message, parent, false)
        }

        val messageTextView = view.findViewById<TextView>(R.id.tv_message)
        val dateTextView = view.findViewById<TextView>(R.id.tv_date)

        val message = getItem(position)

        messageTextView.text = message.text
        dateTextView.text = message.date

        return view
    }
}