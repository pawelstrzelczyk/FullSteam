package com.example.fullsteam.fragments

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.fullsteam.R
import com.example.fullsteam.animation.startAnimation
import com.example.fullsteam.firebase.GlideApp
import com.example.fullsteam.models.Trip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import java.time.Duration
import java.time.LocalTime
import androidx.compose.ui.graphics.Color as GraphicsColor


class TripDetailsFragment : Fragment() {
    private var documentId: String? = null
    private lateinit var trip: Trip
    private val database = FirebaseFirestore.getInstance()
    private lateinit var sharedPref: SharedPreferences
    private lateinit var uId: String
    private val DOCUMENT_ID = "documentId"
    //fields

    private lateinit var trainName: TextView
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
    private lateinit var imageView: ImageView
    private lateinit var couchettePriceLayout: LinearLayout
    private lateinit var bikePriceLayout: LinearLayout
    private lateinit var fabOvalView: View
    private lateinit var fabEdit: FloatingActionButton


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
        imageView = requireActivity().findViewById<ImageView>(R.id.main_options_icon)
        val animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.fab_transition).apply {
                duration = 600
                interpolator = AccelerateInterpolator()

            }
        trainName = detailsView.findViewById(R.id.details_train_full_name)
        startStation = detailsView.findViewById(R.id.details_start_station)
        endStation = detailsView.findViewById(R.id.details_end_station)
        departureTime = detailsView.findViewById(R.id.details_departure_time)
        arrivalTime = detailsView.findViewById(R.id.details_arrival_time)
        isBike = detailsView.findViewById(R.id.details_has_bike)
        isPKM = detailsView.findViewById(R.id.details_isPKM)
        isCouchette = detailsView.findViewById(R.id.details_isCouchette)
        isChange = detailsView.findViewById(R.id.details_hasChange)
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
        couchettePriceLayout = detailsView.findViewById(R.id.couchette_price_layout)
        bikePriceLayout = detailsView.findViewById(R.id.bike_price_layout)
        fabEdit = detailsView.findViewById(R.id.edit_trip_fab)
        fabOvalView = detailsView.findViewById(R.id.fab_oval_view)

        val action = R.id.action_tripDetailsFragment_to_editTripFragment
        val bundle = Bundle()
        bundle.putString("documentId", documentId)
        fabEdit.setOnClickListener {
            fabEdit.visibility = View.INVISIBLE
            fabOvalView.visibility = View.VISIBLE
            fabOvalView.startAnimation(animation) {
                requireView().findNavController().navigate(action, bundle)
            }
        }



        uId = sharedPref.getString(
            getString(R.string.firebase_user_uid),
            "uid could not be retrieved"
        ).toString()

        val userProfilePictureUri = sharedPref.getString(
            getString(R.string.firebase_user_photo_uri),
            "https://d-art.ppstatic.pl/kadry/k/r/1/48/87/60b0e7199f830_o_large.jpg"
        ).toString()

        documentId?.let {
            database.collection("users").document(uId).collection("trips")
                .document(documentId.toString())
                .get().addOnSuccessListener { documentSnapshot ->

                    if (documentSnapshot != null) {
                        val storageReference =
                            FirebaseStorage.getInstance().reference.child("train_pics")
                        var imageReference: StorageReference
                        documentId?.let {
                            try {
                                imageReference = storageReference.child(it)
                                imageReference.downloadUrl.addOnSuccessListener {
                                    GlideApp.with(this).load(imageReference).into(imageView)
                                    Log.d("duupa", it.toString())
                                }


                            } catch (e: StorageException) {
                                GlideApp.with(this).load(userProfilePictureUri).into(imageView)
                            }
                        }

                        trip = documentSnapshot.toObject(Trip::class.java)!!
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
                        val bikePriceValue = trip.bikePrice
                        if (bikePriceValue > 0) {
                            bikePrice.text = bikePriceValue.toString()
                            bikePriceLayout.visibility = View.VISIBLE
                        }
                        val couchettePriceValue = trip.couchettePrice
                        if (couchettePriceValue > 0) {
                            couchettePrice.text = couchettePriceValue.toString()
                            couchettePriceLayout.visibility = View.VISIBLE
                        }

                        pricePerKm.text = buildString {
                            append(trip.pricePerKm)
                            append(" ")
                            append(trip.currency)
                            append("/km")
                        }
                        avgSpeed.text = buildString {
                            append(trip.avgSpeed)
                            append(" km/h")
                        }
                        (trip.price + trip.bikePrice + trip.couchettePrice).toString()
                            .also { totalPrice.text = it }


                        if (trip.departureDelay > 0) {
                            departureTime.paintFlags =
                                departureTime.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                            departureTimeDelayed.text =
                                LocalTime.parse(departureTime.text.toString())
                                    .plus(Duration.ofMinutes(trip.departureDelay.toLong()))
                                    .toString()
                        }
                        if (trip.delay > 0) {
                            arrivalTime.paintFlags =
                                arrivalTime.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                            arrivalTimeDelayed.text = LocalTime.parse(arrivalTime.text.toString())
                                .plus(Duration.ofMinutes(trip.delay.toLong())).toString()
                        }
                        when (trip.delay) {
                            in 1..30 -> {
                                arrivalTime.setTextColor(
                                    GraphicsColor(
                                        105,
                                        194,
                                        125,
                                        255
                                    ).hashCode()
                                )
                            }
                            in 31..60 -> {
                                arrivalTime.setTextColor(
                                    GraphicsColor(
                                        245,
                                        170,
                                        78,
                                        255
                                    ).hashCode()
                                )
                            }
                            in 61..120 -> {
                                arrivalTime.setTextColor(GraphicsColor(252, 66, 66, 255).hashCode())
                            }
                            in 121..Int.MAX_VALUE -> {
                                arrivalTime.setTextColor(GraphicsColor(0, 0, 0, 255).hashCode())

                            }
                        }
                        when (trip.departureDelay) {
                            in 1..30 -> {
                                departureTime.setTextColor(
                                    GraphicsColor(
                                        105,
                                        194,
                                        125,
                                        255
                                    ).hashCode()
                                )
                            }
                            in 31..60 -> {
                                departureTime.setTextColor(
                                    GraphicsColor(
                                        245,
                                        170,
                                        78,
                                        255
                                    ).hashCode()
                                )
                            }
                            in 61..120 -> {
                                departureTime.setTextColor(
                                    GraphicsColor(
                                        252,
                                        66,
                                        66,
                                        255
                                    ).hashCode()
                                )
                            }
                            in 121..Int.MAX_VALUE -> {
                                departureTime.setTextColor(GraphicsColor(0, 0, 0, 255).hashCode())

                            }
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

    override fun onDetach() {
        imageView.setImageResource(R.color.ppMain)
        super.onDetach()
    }

}


