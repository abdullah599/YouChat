package com.example.YouChat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(var messages: ArrayList<Message>, var context: Context) :
	RecyclerView.Adapter<RecyclerView.ViewHolder>() {
	val SENDER = 1
	val RECEIVER = 2


	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		if(viewType==SENDER){
			var view=LayoutInflater.from(context).inflate(R.layout.sender_message,parent,false)
			return senderMessageHolder(view)
		}
		else{
			var view=LayoutInflater.from(context).inflate(R.layout.receiver_message,parent,false)
			return receiverMessageHolder(view)
		}
	}

	override fun getItemViewType(position: Int): Int {
		if (messages[position].sender_id == FirebaseAuth.getInstance().currentUser!!.uid) return SENDER
		else return RECEIVER
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		if (messages[position].sender_id == FirebaseAuth.getInstance().currentUser!!.uid) {
			holder.itemView.findViewById<TextView>(R.id.stv).text=messages[position].message
		}
		else{
			holder.itemView.findViewById<TextView>(R.id.rtv).text=messages[position].message
		}

	}

	override fun getItemCount(): Int {
		return messages.size
	}

	inner class senderMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

	}

	inner class receiverMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

	}
}