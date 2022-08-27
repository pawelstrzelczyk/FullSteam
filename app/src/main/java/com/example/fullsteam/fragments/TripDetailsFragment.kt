package com.example.fullsteam.fragments

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.fullsteam.R
import com.example.fullsteam.firebase.GlideApp
import com.example.fullsteam.models.Trip
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.time.Duration
import java.time.LocalTime


class TripDetailsFragment : Fragment() {
    private var documentId: String? = null
    private lateinit var trip: Trip
    private val database = FirebaseFirestore.getInstance()
    private lateinit var sharedPref: SharedPreferences
    private lateinit var uId: String
    private val DOCUMENT_ID = "documentId"


    //fields
    private lateinit var trainNumber: TextView
    private lateinit var trainName: TextView
    private lateinit var trainBrand: TextView
    private lateinit var startStation: TextView
    private lateinit var endStation: TextView
    private lateinit var tripDate: TextView
    private lateinit var departureTime: TextView
    private lateinit var departureTimeDelayed: TextView
    private lateinit var arrivalTime: TextView
    private lateinit var arrivalTimeDelayed: TextView
    private lateinit var isBike: TextView
    private lateinit var isPKM: TextView
    private lateinit var isCouchette: TextView
    private lateinit var isChange: TextView
    private lateinit var distance: TextView
    private lateinit var duration: TextView
    private lateinit var price: TextView
    private lateinit var bikePrice: TextView
    private lateinit var couchettePrice: TextView
    private lateinit var totalPrice: TextView
    private lateinit var currency: TextView
    private lateinit var pricePerKm: TextView
    private lateinit var avgSpeed: TextView
    private lateinit var comment: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            documentId = it.getString(DOCUMENT_ID)
        }
        sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val detailsView = inflater.inflate(R.layout.fragment_trip_details, container, false)
        val imageView = requireActivity().findViewById<ImageView>(R.id.main_options_icon)
        //trainNumber = detailsView.findViewById(R.id.details_train_number)
        trainName = detailsView.findViewById(R.id.details_train_full_name)
        //trainBrand = detailsView.findViewById(R.id.details_train_brand)
        startStation = detailsView.findViewById(R.id.details_start_station)
        endStation = detailsView.findViewById(R.id.details_end_station)
        departureTime = detailsView.findViewById(R.id.details_departure_time)
        arrivalTime = detailsView.findViewById(R.id.details_arrival_time)
        isBike = detailsView.findViewById(R.id.details_has_bike)
        isPKM = detailsView.findViewById(R.id.details_isPKM)
        isCouchette = detailsView.findViewById(R.id.details_isCouchette)
        isChange = detailsView.findViewById(R.id.details_hasChange)
        //tripDate = detailsView.findViewById(R.id.)
        distance = detailsView.findViewById(R.id.details_trip_distance)
        duration = detailsView.findViewById(R.id.details_trip_duration)
        price = detailsView.findViewById(R.id.details_summary_ticket_price)
        bikePrice = detailsView.findViewById(R.id.details_summary_bike_price)
        couchettePrice = detailsView.findViewById(R.id.details_summary_couchette_price)
        totalPrice = detailsView.findViewById(R.id.details_summary_whole_price)
        pricePerKm = detailsView.findViewById(R.id.details_price_per_km)
        avgSpeed = detailsView.findViewById(R.id.details_avg_speed)
        departureTimeDelayed = detailsView.findViewById(R.id.details_departure_time_delayed)
        arrivalTimeDelayed = detailsView.findViewById(R.id.details_arrival_time_delayed)

        uId = sharedPref.getString(
            getString(R.string.firebase_user_uid),
            "uid could not be retrieved"
        ).toString()
        documentId?.let {
            database.collection("users").document(uId).collection("trips")
                .document(documentId.toString())
                .get().addOnSuccessListener { documentSnapshot ->

                    if (documentSnapshot != null) {
                        val storageReference =
                            FirebaseStorage.getInstance().reference.child("train_pics")
                        var imageReference = storageReference
                        documentId?.let { imageReference = storageReference.child(it) }
                        GlideApp.with(this).load(imageReference).into(imageView)

                        trip = documentSnapshot.toObject(Trip::class.java)!!
                        //trainNumber.text = trip.trainNumber.toString()
                        //trainBrand.text = trip.trainBrand
                        trainName.text = buildString {
                            append(trip.trainBrand)
                            append("  ")
                            append(trip.trainNumber)
                            append("  ")
                            append(trip.trainName)
                        }
                        startStation.text = trip.startStation
                        endStation.text = trip.endStation
                        departureTime.text = trip.startTime
                        arrivalTime.text = trip.endTime
                        isBike.text = "Bike"
                        isChange.text = "Change"
                        isCouchette.text = "Couchette"
                        isPKM.text = "PKM"
                        distance.text = buildString {
                            append(trip.kmDistance)
                            append(" km")
                        }
                        duration.text =
                            LocalTime.MIN.plus(Duration.ofMinutes(trip.tripTimeInMinutes.toLong()))
                                .toString() + "h (${trip.tripTimeInMinutes} min)"
                        price.text = trip.price.toString()
                        bikePrice.text = trip.bikePrice.toString()
                        couchettePrice.text = trip.couchettePrice.toString()
                        pricePerKm.text = trip.pricePerKm.toString() + " ${trip.currency}/km"
                        avgSpeed.text = trip.avgSpeed.toString() + " km/h"
                        totalPrice.text = "TOTAL "
                        (trip.price + trip.bikePrice + trip.couchettePrice).toString()


                        if (trip.departureDelay > 0) {
                            departureTime.paintFlags =
                                departureTime.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                            departureTime.setTextColor(Color.RED)

                            departureTimeDelayed.text =
                                LocalTime.parse(departureTime.text.toString())
                                    .plus(Duration.ofMinutes(trip.departureDelay.toLong()))
                                    .toString()
                        }

                        if (trip.delay > 0) {
                            arrivalTime.paintFlags =
                                arrivalTime.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                            arrivalTime.setTextColor(Color.RED)

                            arrivalTimeDelayed.text = LocalTime.parse(arrivalTime.text.toString())
                                .plus(Duration.ofMinutes(trip.delay.toLong())).toString()
                        }
                        if (trip.hasBike) {
                            isBike.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                0,
                                0,
                                0,
                                R.drawable.ic_baseline_check_24
                            )
                            isBike.compoundDrawableTintList =
                                ColorStateList.valueOf(Color.parseColor("#198000"))
                        }
                        if (trip.isPKM) {
                            isPKM.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                0,
                                0,
                                0,
                                R.drawable.ic_baseline_check_24
                            )
                            isPKM.compoundDrawableTintList =
                                ColorStateList.valueOf(Color.parseColor("#198000"))
                        }
                        if (trip.isSleepingCar) {
                            isCouchette.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                0,
                                0,
                                0,
                                R.drawable.ic_baseline_check_24
                            )
                            isCouchette.compoundDrawableTintList =
                                ColorStateList.valueOf(Color.parseColor("#198000"))
                        }
                        if (trip.hasChange) {
                            isChange.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                0,
                                0,
                                0,
                                R.drawable.ic_baseline_check_24
                            )
                            isChange.compoundDrawableTintList =
                                ColorStateList.valueOf(Color.parseColor("#198000"))
                        }


                    } else {
                        view?.let { it1 ->
                            Snackbar.make(
                                it1,
                                "Trip not found.",
                                Snackbar.LENGTH_LONG
                            )
                        }
                    }
                }
        }


        return detailsView
    }

}