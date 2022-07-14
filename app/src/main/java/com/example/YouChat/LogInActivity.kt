package com.example.YouChat

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.YouChat.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LogInActivity : AppCompatActivity() {    //Global variable for user data


//	lateinit var Uname: String
//	lateinit var Ucontact: String
//	lateinit var Uuname: String
	lateinit var auth: FirebaseAuth

	//binding
	lateinit var binding: ActivityLoginBinding

	override fun onCreate(savedInstanceState: Bundle?) {

		super.onCreate(savedInstanceState)
		binding = ActivityLoginBinding.inflate(layoutInflater)




		setContentView(binding.root)
		supportActionBar?.hide()

		// For login
		binding.button1.setOnClickListener {
			var email: String? = binding.editText.text.toString()
			var password: String? = binding.editText2.text.toString()
			if (email != null && password != null) {
				checkUser(email, password)
			}
			else Toast.makeText(this, "Empty Email or password", Toast.LENGTH_SHORT).show()
		}        //For sign-up
		binding.lbutton2.setOnClickListener {
			var intent = Intent(this, SignupActivity::class.java)
			startActivity(intent)
		}
	}


	private fun checkUser(email: String, password: String) {
		var found = false
		var count = 1

		val database = FirebaseDatabase.getInstance().getReference("Users")
		database.addValueEventListener(object : ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {
				auth = FirebaseAuth.getInstance()

				if (snapshot.exists()) {

					if (count == 1) {
						for (Snapshot in snapshot.children) {
							count++

							if (Snapshot.child("email").value.toString() == email) {

								if (Snapshot.child("password").value.toString() == password) {
									found = true


								}
								else {                                    //Invalid Password
									Toast.makeText(this@LogInActivity, "Invalid Password",
									               Toast.LENGTH_SHORT).show()
									binding.editText2.text.clear()
								}

								break


							}

						}
						if (!found) {                            //Invalid Username
							Toast.makeText(this@LogInActivity, "Invalid Email", Toast.LENGTH_SHORT)
								.show()
							binding.editText.text.clear()
						}
						else {                            //password and username matches
							auth.signInWithEmailAndPassword(email, password)
								.addOnCompleteListener(this@LogInActivity) { task ->
									if (task.isSuccessful) {                                        // Sign in success, update UI with the signed-in user's information

										val user = auth.currentUser
										var intent =
											Intent(this@LogInActivity, LoggedInActivity::class.java)
										startActivity(intent)

									}
									else {

										Toast.makeText(baseContext, "Authentication failed.",
										               Toast.LENGTH_SHORT).show()

									}
								}
						}

					}
				}
			}

			override fun onCancelled(error: DatabaseError) {
				Toast.makeText(baseContext, "Error", Toast.LENGTH_SHORT).show()
			}
		})

	}


}