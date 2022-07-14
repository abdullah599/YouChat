package com.example.YouChat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.text.isDigitsOnly

import com.example.YouChat.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import java.lang.Exception

class SignupActivity : AppCompatActivity() {
	lateinit var binding: ActivitySignupBinding
	lateinit var auth: FirebaseAuth
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivitySignupBinding.inflate(layoutInflater)
		setContentView(binding.root)
		supportActionBar?.hide()

		binding.sbutton1.setOnClickListener {

			if (binding.seditText1.text.toString().
				isDigitsOnly() && !binding.seditText.text.toString()
					.isNullOrBlank() && !binding.seditText1.text.toString()
					.isNullOrBlank() && !binding.seditText2.text.toString()
					.isNullOrBlank() && !binding.seditText3.text.toString().isNullOrBlank()
			) {
				val name = binding.seditText.text.toString()
				val contact = binding.seditText1.text.toString()
				val email = binding.seditText2.text.toString()
				val password = binding.seditText3.text.toString()

				sendData(name, contact, email, password)


			}
			else {
				Toast.makeText(this, "Enter correct data", Toast.LENGTH_LONG).show()
			}


		}
	}

	private fun sendData(name: String, contact: String, email: String, password: String) {
		var found = false
		var count = 1
		auth = FirebaseAuth.getInstance()
		val database = FirebaseDatabase.getInstance().getReference("Users")
		database.addValueEventListener(object : ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {

				if (snapshot.exists()) {

					if (count == 1) {
						for (Snapshot in snapshot.children) {
							count++
							Log.d("error", "isnide for")
							if (Snapshot.child("email").value.toString() == email) {

								found = true
								Log.d("error", "inside if")
								Toast.makeText(applicationContext, "Email already exist",
								               Toast.LENGTH_SHORT).show()
								break


							}

						}
						if (!found) {


								val database = FirebaseDatabase.getInstance().getReference("Users")



									auth.createUserWithEmailAndPassword(email, password)
										.addOnCompleteListener(this@SignupActivity) { task ->
											if (task.isSuccessful) {
												try {
												// Sign in success, update UI with the signed-in user's information
													val user = User(name, contact, email, password,auth.uid!!)
												database.child(auth.uid!!).setValue(user).addOnCompleteListener {
													Toast.makeText(applicationContext, "Data Entered Succesfully",
													               Toast.LENGTH_SHORT).show()
													binding.seditText.text.clear()
													binding.seditText1.text.clear()
													binding.seditText2.text.clear()
													binding.seditText3.text.clear()
												}.addOnCanceledListener {
													Toast.makeText(applicationContext,
													               "Error Occcured: Please check Internet Connection",
													               Toast.LENGTH_LONG).show()
												}
												var intent = Intent(this@SignupActivity,
												                    LogInActivity::class.java)
												startActivity(intent)
												} catch (e: Exception) {
													Log.d("error","${e.message}")
													Toast.makeText(applicationContext, "Error Occcured: ${e.message}",
													               Toast.LENGTH_SHORT).show()
												}
											}
											else {												// If sign in fails, display a message to the user.

												Toast.makeText(baseContext,
												               "Authentication failed. Please enter valid Email and password",
												               Toast.LENGTH_SHORT).show()
											}
										}




						}
						else {
							Toast.makeText(applicationContext,
							               "Email already exist: PLease enter another email",
							               Toast.LENGTH_LONG).show()
						}

					}
				}
			}

			override fun onCancelled(error: DatabaseError) {
				TODO("Not yet implemented")
			}
		})

	}

}



