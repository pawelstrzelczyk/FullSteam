package com.example.fullsteam

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.fullsteam.models.Trip
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class FirebaseHandler {
    private val database = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
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
        currency: String,
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
            currency,
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


}
