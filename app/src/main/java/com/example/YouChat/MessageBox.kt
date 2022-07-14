package com.example.YouChat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.YouChat.databinding.BoxMessageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageBox : AppCompatActivity() {
	lateinit var binding: BoxMessageBinding
	lateinit var messages: ArrayList<Message>
	lateinit var adapter: MessageAdapter
	lateinit var senderRoom: String
	lateinit var receiverRoom: String

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = BoxMessageBinding.inflate(layoutInflater)
		setContentView(binding.root)

		messages = ArrayList()
		adapter = MessageAdapter(messages, this)
		binding.messageRecycler.adapter = adapter
		binding.messageRecycler.layoutManager = LinearLayoutManager(this)


		var rname: String? = null
		var rUid: String? = null


		try {
			rname = intent.getStringExtra("rName").toString()
			rUid = intent.getStringExtra("rUid").toString()
			supportActionBar?.title = rname


		} catch (e: Exception) {
			var intent = Intent(this, LogInActivity::class.java)

			startActivity(intent)
		}


		//Main Logic for sending messages


		var sUid = FirebaseAuth.getInstance().currentUser!!.uid
		senderRoom = sUid + rUid
		receiverRoom = rUid + sUid
		getMessages()
		binding.imageButton.setOnClickListener() {
			var message = binding.sMessage.text?.toString()
			binding.sMessage.text.clear()
			if (message != null && message != "") uploadMessage(message)
		}


	}

	private fun uploadMessage(message: String) {
//		var database = FirebaseDatabase.getInstance().getReference("Messages")
//		database.child(senderRoom).setValue(Message(message, FirebaseAuth.getInstance().currentUser!!.uid))
//		database.child(receiverRoom)
//			.setValue(Message(message, FirebaseAuth.getInstance().currentUser!!.uid))
//		adapter.notifyDataSetChanged()

		var database = FirebaseDatabase.getInstance().getReference("Messages")
		var key=database.child(senderRoom).push().key
		database.child(senderRoom).child(key!!).setValue(Message(message, FirebaseAuth.getInstance().currentUser!!.uid))
		database.child(receiverRoom).
		child(key!!).setValue(Message(message, FirebaseAuth.getInstance().currentUser!!.uid))
		adapter.notifyDataSetChanged()
	}
	private fun getMessages() {
		val database = FirebaseDatabase.getInstance().getReference("Messages")
		try {
			database.child(receiverRoom).addValueEventListener(object : ValueEventListener {
				override fun onDataChange(snapshot: DataSnapshot) {


					if (snapshot.exists()) {
						messages.clear()
						for (Snapshot in snapshot.children) {

							messages.add(Message(Snapshot.child("message").value.toString(),
							                     Snapshot.child("sender_id").value.toString()))
						}
						adapter.notifyDataSetChanged()


					}
				}

				override fun onCancelled(error: DatabaseError) {

				}
			})
		}
		catch (e:java.lang.Exception){
			Log.d("eqq",e.message!!)
		}

	}
}