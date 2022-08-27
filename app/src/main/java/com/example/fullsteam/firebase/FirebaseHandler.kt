package com.example.fullsteam.firebase

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.example.fullsteam.models.Trip
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FirebaseHandler {
    private val database = FirebaseFirestore.getInstance()
    fun addTrip(
        uId: String,
        documentId: String,
        context: Context,
        dateTime: String,
        trainBrand: String,
        trainNumber: Int,
        trainName: String,
        carrier: String,
        startStation: String,
        startTime: String,
        endStation: String,
        endTime: String,
        kmDistance: Int,
        tripTimeInMinutes: Int,
        price: Double,
        currency: String,
        pricePerKm: Double,
        avgSpeed: Double,
        hasChange: Boolean,
        hasBike: Boolean,
        bikePrice: Double,
        isPKM: Boolean,
        isSleepingCar: Boolean,
        sleepingCarPrice: Double,
        delay: Int,
        departureDelay: Int,
        comment: String
    ) {
        val trip = Trip(
            dateTime,
            trainBrand,
            trainNumber,
            trainName,
            carrier,
            startStation,
            startTime,
            endStation,
            endTime,
            kmDistance,
            tripTimeInMinutes,
            price,
            currency,
            pricePerKm,
            avgSpeed,
            hasChange,
            hasBike,
            bikePrice,
            isPKM,
            isSleepingCar,
            sleepingCarPrice,
            delay,
            departureDelay,
            comment
        )


        database.collection("users").document(uId).collection("trips")
            .document(documentId).set(trip.tripToJson())
            .addOnSuccessListener { documentReference ->
                Toast.makeText(
                    context,
                    "Added trip on ${trip.dateTime} with ${trip.trainBrand} ${trip.trainName}",
                    Toast.LENGTH_LONG
                ).show()
                Log.d(
                    TAG, "DocumentSnapshot added with ID: $documentReference"
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
