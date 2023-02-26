package com.example.bookingapphotel.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookingapphotel.ListDispoRoomHotelActivity
import com.example.bookingapphotel.MainActivity
import com.example.bookingapphotel.R
import com.example.bookingapphotel.RoomHotelModel


//The purpose of this class is to provide structure and logic for linking the data of a list of
// RoomHotelModel model objects to a RecyclerView view, which is a dynamic list view in Android.
// It allows defining how each list item should be displayed and updated in the RecyclerView view
// using the onBindViewHolder method.
//The class also contains an inner class called ViewHolder, which allows organizing all the
// components of each view item that need to be updated when filling each cell of the RecyclerView.

class ListRoomHotelAdapter(
    private val context: ListDispoRoomHotelActivity,
    private val roomList: List<RoomHotelModel>,
    private val layoutid: Int
    ) : RecyclerView.Adapter<ListRoomHotelAdapter.ViewHolder>(){

    //permet de ranger tout les composants à controler
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val roomImage = view.findViewById<ImageView>(R.id.imageHotelPresentation)
        val nomHotel = view.findViewById<TextView>(R.id.nomHotel)
        val petiteDescriptionHotel = view.findViewById<TextView>(R.id.petiteDescriptionHotel)
        val tarifParNuit = view.findViewById<TextView>(R.id.tarifParNuit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layoutid,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //récupérer les information de la chambre
        var currentRoom = roomList[position]

        //utiliser glide pour récupérer l'image à partir de son lien
        Glide.with(context).load((Uri.parse(currentRoom.photo_chambre))).into(holder.roomImage)

        //mettre à jour les élément de la chambre
        holder.nomHotel.text = currentRoom.hotel
        holder.petiteDescriptionHotel.text = currentRoom.petite_description
        holder.tarifParNuit.text = currentRoom.tarif_par_nuit
    }

    override fun getItemCount(): Int = roomList.size

}