package com.example.bookingapphotel

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.Calendar

class SearchActivity : AppCompatActivity(){

    private lateinit var dateTextArrivee: TextView
    private lateinit var dateTextDepart: TextView

    lateinit var value: TextView
    lateinit var buttonIncrement: Button
    lateinit var buttonDecrement: Button
    var count:Int = 0
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var auth: FirebaseAuth
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
        databaseReference=database?.reference!!.child("profile")

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
                R.id.nav_reservation->Toast.makeText(applicationContext,"clicked reservation", Toast.LENGTH_LONG).show()
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
        dateTextArrivee = findViewById(R.id.SearchDateArrivé)

        //prendre l'année, le mois et le jour de Calendar
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

    }

    // This function will permit to change the page according to the connected user

    private fun loadProfile(){

        // We retrieve the email address of the user to post it at in the profile part
        val user =auth.currentUser
        val userReference=databaseReference?.child(user?.uid!!)
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

