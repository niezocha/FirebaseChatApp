package com.example.zofiajanicka.firebasechatapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.ProgressBar
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.util.*


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    val ANONYMOUS = "anonymous"
    val RC_SIGN_IN = 1
    val DEFAULT_MSG_LENGTH_LIMIT = 1000

    private lateinit var listView: ListView
    private lateinit var messageAdapter: ChatAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var editText: EditText
    private lateinit var sendButton: Button
    private lateinit var userName: String
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var list: MutableList<ChatMessage>
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authListener: FirebaseAuth.AuthStateListener
    private lateinit var eventListener: ChildEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userName = ANONYMOUS

        database = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        reference = database.getReference("message")


        progressBar = progress_bar
        listView = messageRecyclerView
        editText = messageEditText
        sendButton = send_button

        list = ArrayList()
        messageAdapter = ChatAdapter(this, R.layout.message_item, list)

        listView.adapter = messageAdapter

        progressBar.visibility = ProgressBar.INVISIBLE

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                sendButton.isEnabled = charSequence.toString().trim { it <= ' ' }.isNotEmpty()
            }
            override fun afterTextChanged(editable: Editable) {}
        })

        sendButton.setOnClickListener {
            val stringDate = android.text.format.DateFormat.format("yyyy-MM-dd HH:mm", java.util.Date()).toString()
            val message = ChatMessage(userName, editText.text.toString(), stringDate)
            reference.push().setValue(message)
            editText.setText("")
        }

        eventListener = object: ChildEventListener{
            override fun onCancelled(dataSnapshot: DatabaseError) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, p1: String?) {}
            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {}
            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                val message: ChatMessage = dataSnapshot.getValue(ChatMessage::class.java)
                messageAdapter.add(message)
            }
            override fun onChildRemoved(p0: DataSnapshot?) {}
        }

        reference.addChildEventListener(eventListener)


        authListener = object: FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
                var user: FirebaseUser? = firebaseAuth.currentUser

                if(user != null) {
                    toast("logowanie powiodło się")
                } else{

                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .build(),
                            RC_SIGN_IN)
                }
            }
        }


    }


    override fun onPause() {
        super.onPause()
        firebaseAuth.removeAuthStateListener(authListener)
    }


    override fun onResume() {
        super.onResume()
        firebaseAuth.addAuthStateListener(authListener)

    }



}
