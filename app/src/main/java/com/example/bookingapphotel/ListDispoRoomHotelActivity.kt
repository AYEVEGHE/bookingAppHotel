package com.example.bookingapphotel

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bookingapphotel.fragments.ListRoomHotelFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ListDispoRoomHotelActivity : AppCompatActivity() {
    private lateinit var NavigationView: NavigationView
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null
    var database : FirebaseDatabase?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_hotel)
        auth= FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance()
        databaseReference=database?.reference!!.child("profile")

        // This method permits to lead the page of the app according to the user profile
        loadProfile()

        //Récupérer valeur de search activity
        val receiverIntent: Intent = getIntent()
        val receivedValueDestination: String? = receiverIntent.getStringExtra("textDestination")

        //charger RoomRepository
        val repo = RoomRepository(receivedValueDestination)

        //mettre à jour la liste des chambres disponible
        repo.updateData{
            //injection du fragment dans "fragment_list_hotel
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_list_hotel, ListRoomHotelFragment(this))
            transaction.addToBackStack(null)
            transaction.commit()
        }

        //Setting of Bar de navigation
        val drawerLayout: DrawerLayout =findViewById(R.id.drawerLayout)
        val navView : NavigationView =findViewById(R.id.nav_view)

        toggle= ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){

                R.id.nav_home->{
                    val intent= Intent(this,SearchActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_reservation->{
                    val user =auth.currentUser
                    val reservationsRef = FirebaseDatabase.getInstance().getReference("reservation")
                    val reservationQuery = reservationsRef.child(user?.uid!!)

                    reservationQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // L'utilisateur a une réservation, on exécute ReservationActivity
                                startActivity(Intent(this@ListDispoRoomHotelActivity, ReservationActivity::class.java))
                            } else {
                                // L'utilisateur n'a pas de réservation, on exécute AucuneReservationActivity
                                startActivity(Intent(this@ListDispoRoomHotelActivity, AucuneReservationActivity::class.java))
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Gestion des erreurs
                        }
                    })
                }
                R.id.nav_logout->{
                    // permet de se deconnecter
                    auth.signOut()
                    val intent= Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(applicationContext,"clicked logout", Toast.LENGTH_LONG).show()
                }
            }
            true
        }

    }


    // This function will permit to change the page according to the connected user

    private fun loadProfile() {
        // We retrieve the email address of the user to post it at in the profile part
        val user = auth.currentUser                                                      // Retrieve the current user
        val userReference = databaseReference?.child(user?.uid!!)                        // Retrieve the reference to the user in the database

        // Add a ValueListener to the user reference to retrieve the user's data
        userReference?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                // When the user's data has changed
                NavigationView = findViewById(R.id.nav_view) // Get the navigation view
                val username = NavigationView.findViewById<TextView>(R.id.user_name)      // Get the TextView element corresponding to the user's name
                username.text = snapshot.child("nom_prenom").value.toString()        // Update the text of the TextView element with the user's name
            }
            override fun onCancelled(error: DatabaseError) {
                // In case of cancellation
                TODO("Not yet implemented")
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // When an options menu item is selected
        if (toggle.onOptionsItemSelected(item)) {
            // If the selected item is the toggle button
            return true // Return true to indicate that the event has been handled
        }
        // Otherwise
        return super.onOptionsItemSelected(item) // Call the superclass's onOptionsItemSelected() method to handle the event
    }
}