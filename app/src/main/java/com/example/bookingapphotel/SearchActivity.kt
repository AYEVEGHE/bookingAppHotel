package com.example.bookingapphotel

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class SearchActivity : AppCompatActivity(){

    private lateinit var dateTextArrivee: TextView
    private lateinit var dateTextDepart: TextView

    lateinit var value: TextView
    lateinit var buttonIncrement: Button
    lateinit var buttonDecrement: Button
    var count:Int = 0
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var auth: FirebaseAuth
    var databaseReferenceProfile: DatabaseReference?=null
    var databaseReferenceReservation: DatabaseReference?=null
    var databaseReference: DatabaseReference?=null
    var database : FirebaseDatabase?=null
    private lateinit var NavigationView: NavigationView


    lateinit var spinner1:Spinner
    lateinit var spinner2:Spinner

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_hotel)

        auth= FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance()
        databaseReferenceProfile=database?.reference!!.child("profile")
        databaseReferenceReservation=database?.reference!!.child("reservation")

        // This method permits to lead the page of the app according to the user profile
        loadProfile()

        spinner1=findViewById(R.id.SpinnerDestination)
        // This method will set the destination available for the user according to what is in the database
        configSprinner("chambre","ville",spinner1)
        // This method will set the type of room available for the user according to what is in the database
        spinner2=findViewById(R.id.SpinnerTypeChambre)
        configSprinner("chambre","categorie",spinner2)

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
                    val intent=Intent(this,SearchActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_reservation->{
                    val user =auth.currentUser
                    val reservationsRef = FirebaseDatabase.getInstance().getReference("reservation")
                    val reservationQuery = reservationsRef.child(user?.uid!!)


                    reservationQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // L'utilisateur a une r??servation, on ex??cute ReservationActivity
                                startActivity(Intent(this@SearchActivity, ReservationActivity::class.java))
                            } else {
                                // L'utilisateur n'a pas de r??servation, on ex??cute AucuneReservationActivity
                                startActivity(Intent(this@SearchActivity, AucuneReservationActivity::class.java))
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Gestion des erreurs
                        }
                    })
                }
                R.id.nav_logout->{
                    auth.signOut()
                    val intent=Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(applicationContext,"clicked logout", Toast.LENGTH_LONG).show()
                }
            }
            true
        }
        // End of the setting of the navigation bar

        // configuration de la date d'arrivee et de retour
        dateTextArrivee = findViewById(R.id.SearchDateArriv??)

        //prendre l'ann??e, le mois et le jour de Calendar
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        dateTextArrivee.setOnClickListener {
            // your code to perform when the user clicks on the TextView
            val dpd = DatePickerDialog(this,DatePickerDialog.OnDateSetListener { view, myear, mmonth, mday ->
                val mmonth = mmonth +1
                val message = " $myear/$mmonth/$mday "
                dateTextArrivee.text = message
            },year,month,day)
            dpd.show()
        }

        dateTextDepart = findViewById(R.id.SearchDateDepart)

        dateTextDepart.setOnClickListener {
            // your code to perform when the user clicks on the TextView
            val dpd = DatePickerDialog(this,DatePickerDialog.OnDateSetListener { view, myear, mmonth, mday ->
                val mmonth = mmonth +1
                val message = " $myear/$mmonth/$mday "
                dateTextDepart.text = message
            },year,month,day)
            dpd.show()
        }


        value = findViewById(R.id.SearchNombrePersonneBox)
        buttonIncrement = findViewById(R.id.SearchPersonneIncrement)
        buttonDecrement = findViewById(R.id.SearchPersonneDecrement)

        buttonIncrement.setOnClickListener{
            count++
            value.text = count.toString()
        }

        buttonDecrement.setOnClickListener{
            if (count<=1){
                count = 1
            }
            else{
                count--
            }
            value.text = count.toString()
        }
        val btnRechercher: Button =findViewById(R.id.editSearchListDisplay)

        // When we click on Enter button, it will do the activity ConnexionActivity
        btnRechercher.setOnClickListener{
            val spinnerDestination: Spinner = findViewById(R.id.SpinnerDestination)
            val textDestination: String = spinnerDestination.selectedItem.toString()

            val dateDebut: TextView = findViewById(R.id.SearchDateArriv??)
            val textDateDebut: String = dateDebut.toString()

            val dateFin: TextView = findViewById(R.id.SearchDateArriv??)
            val textDateFin: String = dateFin.toString()

            val intent=Intent(this,ListDispoRoomHotelActivity::class.java)

            intent.putExtra("textDestination",textDestination)
            intent.putExtra("textDebut",textDateDebut)
            intent.putExtra("textFin",textDateFin)
            startActivity(intent)

        }



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

    // This methode will configure the spinners of the page according to what is in the database

    private fun configSprinner(table:String, attribut:String,spinner: Spinner){
        databaseReference=FirebaseDatabase.getInstance().getReference()
        var listDestination:MutableList<String> = ArrayList()
        val chambreReference=databaseReference?.child(table)

        chambreReference?.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                for (childSnapshot in snapshot.children) {
                    var destination: String=childSnapshot.child(attribut).value.toString()
                    if ( destination !in listDestination) {
                        listDestination.add(destination)
                    }
                }
                val arrayAdapter = ArrayAdapter(this@SearchActivity, android.R.layout.simple_list_item_1,listDestination)
                spinner.adapter=arrayAdapter

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    }

