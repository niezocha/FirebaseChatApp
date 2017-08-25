package com.example.zofiajanicka.firebasechatapp

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.message_item.view.*

class ChatAdapter(context: Context, resource: Int, objects: List<ChatMessage>) : ArrayAdapter<ChatMessage>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = (context as Activity).layoutInflater.inflate(R.layout.message_item, parent, false)
        }

        val messageTextView = convertView?.findViewById(R.id.tv_message) as TextView
        val authorTextView = convertView.findViewById(R.id.tv_user) as TextView
        val dateTextView = convertView.findViewById(R.id.tv_date) as TextView

        val message = getItem(position)

        messageTextView.text = message.text
        authorTextView.text = message.user
        dateTextView.text = message.date

        return convertView
    }
}