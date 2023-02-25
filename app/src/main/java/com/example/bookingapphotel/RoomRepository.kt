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
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //retirer les anciennes valeurs
                roomList.clear()
                //récypérer la liste
                for(ds in snapshot.children){
                    //construire un objet chambre
                    val room = ds.getValue(RoomHotelModel::class.java)

                    //vérifier que la chambre n'est pas null
                    if (room!=null){
                        roomList.add(room)
                    }
                }
                //actionner le callback
                callback()
            }

            override fun onCancelled(error: DatabaseError) {}

        } )
    }

}