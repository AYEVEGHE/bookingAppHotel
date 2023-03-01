package com.example.bookingapphotel.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.bookingapphotel.ListDispoRoomHotelActivity
import com.example.bookingapphotel.R
import com.example.bookingapphotel.RoomRepository.Singleton.roomList
import com.example.bookingapphotel.adapter.HotelImageDecoration
import com.example.bookingapphotel.adapter.ListRoomHotelAdapter

class ListRoomHotelFragment (
    private val context: ListDispoRoomHotelActivity
        ): Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_dispo_room_hotel, container, false)


        //récupérer le RecyclerView
        val verticalRecyclerView = view!!.findViewById<RecyclerView>(R.id.horizontal_recycler_view)
        //roomList est un liste qui permet de stocker les chambres d'hotel
        //elle est déclarer dans "RoomRepository" qui a été importer
        verticalRecyclerView.adapter = ListRoomHotelAdapter(context, roomList, R.layout.item_vertical_room)
        verticalRecyclerView.addItemDecoration(HotelImageDecoration())

        return view
        //return inflater?.inflate(R.layout.fragment_test, container, false)
    }
}