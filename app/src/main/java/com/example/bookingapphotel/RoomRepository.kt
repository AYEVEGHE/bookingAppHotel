package com.example.bookingapphotel

import com.example.bookingapphotel.RoomRepository.Singleton.databaseRef
import com.example.bookingapphotel.RoomRepository.Singleton.roomList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RoomRepository {
    object Singleton{
        //se connecter à la référence "chambre de firebase
        val databaseRef = FirebaseDatabase.getInstance().getReference("chambre")

        //creation d'une liste qui contient les chambres
        val roomList = arrayListOf<RoomHotelModel>()
    }




    fun updateData(callback: () -> Unit){
        // Add a listener to retrieve data from the database
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Remove old values from the list of rooms
                roomList.clear()
                // Iterate over all entries in the database to retrieve the list of rooms
                for(ds in snapshot.children){
                    // Build a RoomHotelModel object from the data of each entry
                    val room = ds.getValue(RoomHotelModel::class.java)

                    // Check that the room is not null
                    if (room!=null){
                        roomList.add(room)
                    }
                }
                // Trigger the callback after retrieving data from the database
                callback()
            }

            override fun onCancelled(error: DatabaseError) {
                // In case of cancellation (not implemented)
            }
        })
    }








}