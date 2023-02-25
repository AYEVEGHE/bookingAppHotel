package com.example.bookingapphotel

import com.example.bookingapphotel.RoomRepository.Singleton.databaseRef
import com.example.bookingapphotel.RoomRepository.Singleton.roomList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RoomRepository {
    object Singleton{
        val databaseRef = FirebaseDatabase.getInstance().getReference("chambre")

        val roomList = arrayListOf<RoomHotelModel>()
    }

    fun updateData(callback: () -> Unit){
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                roomList.clear()
                for(ds in snapshot.children){
                    val room = ds.getValue(RoomHotelModel::class.java)

                    if (room!=null){
                        roomList.add(room)
                    }
                }
                //actionner lecallback
                callback()
            }

            override fun onCancelled(error: DatabaseError) {}

        } )
    }

}