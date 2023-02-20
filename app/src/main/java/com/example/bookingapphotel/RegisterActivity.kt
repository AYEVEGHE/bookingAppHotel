package com.example.bookingapphotel

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_account)

        // initialize Firebase Auth
        auth = Firebase.auth

        // Attribution of the function of th Enter button
        // We want this sentence: "Identifiez-vous" be clickable

        val connection:TextView=findViewById(R.id.textView4)
        val text:String  ="Identifiez-vous"

        val spannable = SpannableString(text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                // Do something when the clickable part of the text is clicked
                val intent= Intent(view.context,MainActivity::class.java)
                startActivity(intent)

            }
        }
        spannable.setSpan(clickableSpan, 0, 15, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        connection.text = spannable
        // allow the TextView to handle clicks on the clickable part of the text.
        connection.movementMethod = LinkMovementMethod.getInstance()



        var editEntrer: Button = findViewById(R.id.editEntrer)
        editEntrer.setOnClickListener {
            signUp()

        }
    }
        // lets get email and password from the user

        private fun signUp(){
            val editEmail=findViewById<EditText>(R.id.editEmail)
            val editPassword=findViewById<EditText>(R.id.editPassword)

            // before registering the new user we will check if there all the blanks are full

            if (editEmail.text.isEmpty()|| editPassword.text.isEmpty()){
                Toast.makeText(this,"Please fill all fields", Toast.LENGTH_SHORT).show()
                return
            }
            val email=editEmail.text.toString()
            val password =editPassword.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){
                    task ->
                    if (task.isSuccessful) {
                        // Sign in success,lets move to the next activity
                        Toast.makeText(baseContext,"Authentication succeeded.",Toast.LENGTH_SHORT).show()
                    }else{
                        //If sign in fails, display a message to the user.
                        Toast.makeText(baseContext,"Authentication failed.",Toast.LENGTH_SHORT).show()
                    }

                }.addOnFailureListener{
                    Toast.makeText(this, "Error occurred ${it.localizedMessage}",Toast.LENGTH_SHORT).show()

                }



        }



}