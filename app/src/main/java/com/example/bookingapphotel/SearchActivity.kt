package com.example.bookingapphotel

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.core.view.View
import java.util.Calendar

class SearchActivity : AppCompatActivity(){

    private lateinit var dateTextArrivee: TextView
    private lateinit var dateTextDepart: TextView

    lateinit var value: TextView
    lateinit var buttonIncrement: Button
    lateinit var buttonDecrement: Button
    var count:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_hotel)

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



}