package com.example.YouChat


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.YouChat.databinding.ActivityLoggedInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LoggedInActivity : AppCompatActivity() {
	lateinit var binding: ActivityLoggedInBinding
	lateinit var auth: FirebaseAuth

	lateinit var Users: ArrayList<User>
	lateinit var adapter: chatAdapter //for adapter

	lateinit var name: String
	lateinit var email: String
	lateinit var contact: String
	override fun onCreate(savedInstanceState: Bundle?) {


		super.onCreate(savedInstanceState)
		binding = ActivityLoggedInBinding.inflate(layoutInflater)
		if (FirebaseAuth.getInstance().currentUser != null) {
			login(FirebaseAuth.getInstance().currentUser!!)
		}
		else {
			var intent = Intent(this, LogInActivity::class.java)
			startActivity(intent)
		}
		setTheme(R.style.Theme_Dodo)
		setContentView(binding.root)



		auth = FirebaseAuth.getInstance()
		if (auth.currentUser == null) {
			var intent = Intent(this, LogInActivity::class.java)

			startActivity(intent)
		}

		Users = ArrayList()
		getUsers()
		adapter = chatAdapter(Users, this)
		binding.chatRecyclerView.adapter = adapter
		binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)


	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.top_menu, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.logut -> {
				auth.signOut()
				var intent = Intent(this, LogInActivity::class.java)
				startActivity(intent)
			}
		}
		return super.onOptionsItemSelected(item)
	}

	private fun getUsers() {


		val database = FirebaseDatabase.getInstance().getReference("Users")
		database.addValueEventListener(object : ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {


				if (snapshot.exists()) {
					Users.clear()
					for (Snapshot in snapshot.children) {
						if (Snapshot.key != auth.uid) Users.add(
							User(Snapshot.child("name").value.toString(),
							     Snapshot.child("contact").value.toString(),
							     Snapshot.child("email").value.toString(),
							     Snapshot.child("password").value.toString(),
							     Snapshot.child("uid").value.toString()))
					}
					adapter.notifyDataSetChanged()


				}
			}

			override fun onCancelled(error: DatabaseError) {
				TODO("Not yet implemented")
			}
		})

	}

	private fun login(user: FirebaseUser) {

		val database = FirebaseDatabase.getInstance().getReference("Users")
		database.addValueEventListener(object : ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {
				auth = FirebaseAuth.getInstance()

				if (snapshot.exists()) {


					for (Snapshot in snapshot.children) {

						Log.d("users", "${Snapshot.child("email").value.toString()}")
						if (Snapshot.child("email").value.toString() == user.email.toString()) {


							name = Snapshot.child("name").value.toString()

							supportActionBar?.title = name

							email = Snapshot.child("email").value.toString()
							contact = Snapshot.child("contact").value.toString()
							val user = auth.currentUser


						}


					}


					// Sign in success, update UI with the signed-in user's information


				}

			}


			override fun onCancelled(error: DatabaseError) {
				Toast.makeText(baseContext, "Error", Toast.LENGTH_SHORT).show()
			}
		})
	}
}