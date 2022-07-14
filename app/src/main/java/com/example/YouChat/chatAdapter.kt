package com.example.YouChat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class chatAdapter(var User:ArrayList<User>, var context: Context):RecyclerView.Adapter<chatAdapter.chatViewHolder>() {
	inner class chatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
		var name:TextView=itemView.findViewById(R.id.row_name)
		var contact:TextView=itemView.findViewById(R.id.row_contact)


	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): chatViewHolder {
		val view=LayoutInflater.from(parent.context).inflate(R.layout.main_chat_row_layout, parent ,false)
		return chatViewHolder(view)
	}

	override fun onBindViewHolder(holder: chatViewHolder, position: Int) {
		holder.name.text=User[position].name
		holder.contact.text=User[position].email
		holder.itemView.setOnClickListener{
			var intent= Intent(context, MessageBox::class.java)

			intent.putExtra("rName",User[position].name)
			intent.putExtra("rUid",User[position].Uid)

			context.startActivity(intent)

		}
	}

	override fun getItemCount(): Int {
		return User.size
	}
}