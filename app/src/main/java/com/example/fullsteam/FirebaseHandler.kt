package com.example.fullsteam

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.fullsteam.models.Trip
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseHandler {
    val database = FirebaseFirestore.getInstance()
    private val trips: ArrayList<Trip> = arrayListOf()
    fun addTrip(
        context: Context,
        dateTime: String,
        trainBrand: String,
        trainNumber: Int,
        trainName: String,
        carrier: String,
        startStation: String,
        endStation: String,
        kmDistance: Int,
        tripTimeInMinutes: Int,
        price: Double,
        pricePerKm: Double,
        avgSpeed: Double,
        hasChange: Boolean,
        hasBike: Boolean,
        isPKM: Boolean,
        isSleepingCar: Boolean,
        delay: Int,
        comment: String
    ) {
        val trip = Trip(
            dateTime,
            trainBrand,
            trainNumber,
            trainName,
            carrier,
            startStation,
            endStation,
            kmDistance,
            tripTimeInMinutes,
            price,
            pricePerKm,
            avgSpeed,
            hasChange,
            hasBike,
            isPKM,
            isSleepingCar,
            delay,
            comment
        )


        database.collection("trips")
            .add(trip.tripToJson())
            .addOnSuccessListener { documentReference ->
                Toast.makeText(
                    context,
                    "Added trip on ${trip.dateTime} with ${trip.trainBrand} ${trip.trainName}",
                    Toast.LENGTH_LONG
                ).show()
                Log.d(
                    TAG, "DocumentSnapshot added with ID: " + documentReference.id
                )
                Log.d(
                    TAG, "Train added: " + trip.dateTime + trip.trainBrand + trip.trainName
                )
            }
            .addOnFailureListener { e ->
                Log.w(
                    TAG,
                    "Error adding document",
                    e
                )
            }
    }


    fun getTrips() {
        database.collection("trips").get().addOnSuccessListener { documents ->
//                Toast.makeText(
//                    context,
//                    "Downloaded trips!",
//                    Toast.LENGTH_LONG
//                ).show()
            Log.w(
                ContentValues.TAG,
                "Downloaded documents! ${documents.size()}"
            )
            for (document in documents) {
                val obj = document.toObject(Trip::class.java)
                trips.add(obj.copy())
            }

        }.addOnFailureListener { e ->
            Log.w(
                ContentValues.TAG,
                "Error downloading documents:c",
                e
            )
        }.addOnSuccessListener {

        }
    }

}
