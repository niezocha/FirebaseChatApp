package com.example.zofiajanicka.firebasechatapp

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener, TextWatcher, FirebaseAuth.AuthStateListener,LifecycleRegistryOwner {

    private val TAG = "MainActivity"

    private val registry = LifecycleRegistry(this)

    val ANONYMOUS = "anonymous"
    val RC_SIGN_IN = 1
    val DEFAULT_MSG_LENGTH_LIMIT = 1000

    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: LinearLayoutManager
    private lateinit var editText: EditText
    private lateinit var sendButton: Button
    private lateinit var userName: String
    private lateinit var receiverName: String
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var auth:FirebaseAuth
    private lateinit var adapter: FirebaseRecyclerAdapter<ChatMessage, ChatHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        auth.addAuthStateListener(this)

        userName = auth.currentUser?.uid ?: ANONYMOUS
        receiverName = "other guy"
        editText = messageEditText
        sendButton = send_button

        manager = LinearLayoutManager(this)

        recyclerView = messageRecyclerView
        recyclerView.layoutManager = manager

        database = FirebaseDatabase.getInstance()
        reference = database.reference

        editText.addTextChangedListener(this)
        sendButton.setOnClickListener(this)

        if(isSigniIn()){ attachRecyclerViewAdapter() }

    }

    override fun onStart() {
        super.onStart()
        signInAnonim()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.cleanup()
    }

    private fun attachRecyclerViewAdapter(){
        adapter = getAdapter()
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                manager.smoothScrollToPosition(recyclerView, null, adapter.itemCount)
            }
        })
        recyclerView.adapter = adapter
    }

    private fun getAdapter(): FirebaseRecyclerAdapter<ChatMessage, ChatHolder>{

        return object : FirebaseRecyclerAdapter<ChatMessage, ChatHolder>(
                ChatMessage::class.java,
                R.layout.item_message,
                ChatHolder::class.java,
                reference,
                this) {

            public override fun populateViewHolder(holder: ChatHolder, chat: ChatMessage, position: Int) {
                holder.bind(chat)
            }

            override fun onDataChanged() {
                super.onDataChanged()
            }
        }
    }

    override fun onClick(view: View?) {
        val stringDate = android.text.format.DateFormat.format("yyyy-MM-dd HH:mm", java.util.Date()).toString()
        val message = ChatMessage(userName, receiverName, editText.text.toString(), stringDate)
        reference.push().setValue(message)
        editText.setText("")
    }

    private fun updateUi(){
        sendButton.isEnabled = isSigniIn()
        editText.isEnabled = isSigniIn()
    }

    private fun signInAnonim(){
        auth.signInAnonymously()
                .addOnSuccessListener(this, { attachRecyclerViewAdapter() })
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
        updateUi()
    }

    private fun isSigniIn():Boolean = auth.currentUser != null

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
    }

    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        sendButton.isEnabled = charSequence.toString().trim { it <= ' ' }.isNotEmpty()
    }

    override fun afterTextChanged(editable: Editable) {
    }

    override fun getLifecycle(): LifecycleRegistry = registry

}
