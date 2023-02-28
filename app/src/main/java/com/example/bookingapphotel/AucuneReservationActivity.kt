package com.example.bookingapphotel

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AucuneReservationActivity: AppCompatActivity(){


    lateinit var toggle: ActionBarDrawerToggle
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null
    var database : FirebaseDatabase?=null
    private lateinit var NavigationView: NavigationView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.aucune_reservation)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        // This method permits to lead the page of the app according to the user profile
        loadProfile()


        //Setting of Bar de navigation
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.nav_home -> {
                    val intent = Intent(this, SearchActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_reservation -> {
                    val user =auth.currentUser
                    val reservationsRef = FirebaseDatabase.getInstance().getReference("reservation")
                    val reservationQuery = reservationsRef.child(user?.uid!!)


                    reservationQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // L'utilisateur a une réservation, on exécute ReservationActivity
                                startActivity(Intent(this@AucuneReservationActivity, ReservationActivity::class.java))
                            } else {
                                // L'utilisateur n'a pas de réservation, on exécute AucuneReservationActivity
                                startActivity(Intent(this@AucuneReservationActivity, AucuneReservationActivity::class.java))
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Gestion des erreurs
                        }
                    })
                }

                R.id.nav_logout -> {
                    auth.signOut()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(applicationContext, "clicked logout", Toast.LENGTH_LONG).show()
                }
            }
            true
        }

        // End of the setting of the navigation bar

    }
        // This function will permit to change the page according to the connected user

        private fun loadProfile(){

            // We retrieve the email address of the user to post it at in the profile part
            val user =auth.currentUser
            val userReference=databaseReference?.child(user?.uid!!)
            userReference?.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot){
                    NavigationView = findViewById(R.id.nav_view)
                    val username = NavigationView.findViewById<TextView>(R.id.user_name)
                    username.text=snapshot.child("nom_prenom").value.toString()
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            if(toggle.onOptionsItemSelected(item)){
                return true
            }
            return super.onOptionsItemSelected(item)
        }
}
