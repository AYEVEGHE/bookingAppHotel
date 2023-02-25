package com.example.bookingapphotel

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bookingapphotel.fragments.ListRoomHotelFragment

class ListDispoRoomHotelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_hotel)

        //charger RoomRepository
        val repo = RoomRepository()

        //mettre à jour la liste des chambres disponible
        repo.updateData{
            //injection du fragment dans "fragment_list_hotel
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_list_hotel, ListRoomHotelFragment(this))
            transaction.addToBackStack(null)
            transaction.commit()
        }


    }
}