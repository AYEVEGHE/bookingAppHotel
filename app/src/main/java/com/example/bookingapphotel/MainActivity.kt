package com.example.bookingapphotel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    // lateinit= we initialise the variable later on


    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth= Firebase.auth
        val creationCompte: TextView=findViewById(R.id.textView4)
        val text: String ="Pas de compte? Creer "

        val spannable = SpannableString(text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                // Do something when the clickable part of the text is clicked
                val intent=Intent(view.context,RegisterActivity::class.java)
                startActivity(intent)

            }
        }

        spannable.setSpan(clickableSpan, 15, 20, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        creationCompte.text = spannable
        // allow the TextView to handle clicks on the clickable part of the text.
        creationCompte.movementMethod = LinkMovementMethod.getInstance()

        val btnconnexion: Button =findViewById(R.id.editEntrer)

        // When we click on Enter button, it will do the activity ConnexionActivity
        btnconnexion.setOnClickListener{
            login()

        }


    }
    private fun login(){
        // get input from the user
        val editEmail=findViewById<EditText>(R.id.editEmail)
        val editPassword=findViewById<EditText>(R.id.editPassword)

        //null checks on inputs
        if (editEmail.text.isEmpty()|| editPassword.text.isEmpty()){
            Toast.makeText(this,"Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        val email=editEmail.text.toString()
        val password =editPassword.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                    task ->
                if (task.isSuccessful) {
                    // Sign in success,lets move to the next activity
                    //Toast.makeText(baseContext,"Authentication succeeded.",Toast.LENGTH_SHORT).show()
                    val intent=Intent(this,SearchActivity::class.java)
                    startActivity(intent)
                }else{
                    //If sign in fails, display a message to the user.
                    Toast.makeText(baseContext,"Authentication failed.",Toast.LENGTH_SHORT).show()
                }

            }.addOnFailureListener{
                Toast.makeText(this, "Error occurred ${it.localizedMessage}",Toast.LENGTH_SHORT).show()

            }
    }
}