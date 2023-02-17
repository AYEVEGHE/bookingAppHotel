package com.example.bookingapphotel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // ligne de teste de l'implementation de la base de donnée
        val firebase: DatabaseReference= FirebaseDatabase.getInstance().getReference("laurie")


        // modif emma
        // test
        // modif emma 2
        //autre test


    }
}