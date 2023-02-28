package com.example.bookingapphotel

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class ReservationActivity: AppCompatActivity() {


    lateinit var toggle: ActionBarDrawerToggle
    lateinit var auth: FirebaseAuth
    var databaseReferenceProfile: DatabaseReference? = null
    var databaseReferenceReservation: DatabaseReference? = null
    var database: FirebaseDatabase? = null
    private lateinit var NavigationView: NavigationView
    lateinit var nom:TextView
    lateinit var nom_hotel:TextView
    lateinit var type_chambre:TextView
    lateinit var nombre_de_personne: TextView
    lateinit var date_arrivee:TextView
    lateinit var date_fin:TextView
    lateinit var prix:TextView
    lateinit var photo_hotel:ImageView
    lateinit var photo_chambre:ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reservation)

        auth= FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance()
        databaseReferenceReservation=database?.reference!!.child("reservation")
        databaseReferenceProfile=database?.reference!!.child("profile")
        // This method permits to lead the page of the app according to the user profile
        loadProfile()
        // call of the function to launch the data of the reservation
        affichage_reservation()


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
                                startActivity(Intent(this@ReservationActivity, ReservationActivity::class.java))
                            } else {
                                // L'utilisateur n'a pas de réservation, on exécute AucuneReservationActivity
                                startActivity(Intent(this@ReservationActivity, AucuneReservationActivity::class.java))
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Gestion des erreurs
                        }
                    })
                }

                R.id.nav_logout->{
                    auth.signOut()
                    val intent= Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(applicationContext,"clicked logout", Toast.LENGTH_LONG).show()
                }
            }
            true
        }
        // End of the setting of the navigation bar

    }
    private fun affichage_reservation() {
        val user = auth.currentUser
        // We retrieve the view we need to fill we the data from the database
        nom = findViewById(R.id.nomreservation)
        nom_hotel=findViewById(R.id.nom_hotel)
        type_chambre=findViewById(R.id.type_chambre)
        nombre_de_personne=findViewById(R.id.nombre_de_personne)
        date_arrivee=findViewById(R.id.date_arrivee)
        date_fin=findViewById(R.id.date_fin)
        prix=findViewById(R.id.prix)
        photo_hotel=findViewById(R.id.imagehotel)
        photo_chambre=findViewById(R.id.imageChambre)


        // Référence à l'emplacement de l'image dans Firebase Storage



        val userReference = databaseReferenceReservation?.child(user?.uid!!)
        userReference?.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                nom.text = "Nom: "+ snapshot.child("nom_prenom").value.toString()
                nom_hotel.text="Hotel: "+ snapshot.child("nom_hotel").value.toString()
                type_chambre.text="Type de chambre: "+ snapshot.child("type_de_chambre").value.toString()
                nombre_de_personne.text="Nombre de personnes: "+ snapshot.child("nombre_de_personne").value.toString()
                date_arrivee.text="Date d'arrivée: "+ snapshot.child("date_de_debut").value.toString()
                date_fin.text="Date de départ: "+ snapshot.child("date_de_fin").value.toString()
                prix.text=snapshot.child("prix").value.toString()
                // Récupérer l'URL de l'image à partir de Realtime Database
                val imageUrl_hotel= snapshot.child("photo_hotel").getValue(String::class.java)
                // Charger l'image dans l'ImageView à l'aide de Picasso
                Picasso.get().load(imageUrl_hotel).into(photo_hotel)
                // Récupérer l'URL de l'image à partir de Realtime Database
                val imageUrl_chambre= snapshot.child("photo_chambre").getValue(String::class.java)
                // Charger l'image dans l'ImageView à l'aide de Picasso
                Picasso.get().load(imageUrl_chambre).into(photo_chambre)
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    // This function will permit to change the page according to the connected user

    private fun loadProfile(){
        // We retrieve the email address of the user to post it at in the profile part
        val user =auth.currentUser
        val userReference=databaseReferenceProfile?.child(user?.uid!!)
        userReference?.addValueEventListener(object:ValueEventListener{
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