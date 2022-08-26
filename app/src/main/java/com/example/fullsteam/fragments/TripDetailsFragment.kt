package com.example.fullsteam.fragments

import android.content.Context
import android.content.SharedPreferences
import android.media.Image
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

private const val DOCUMENT_ID = "documentId"

class TripDetailsFragment : Fragment() {
    private var documentId: String? = null
    private var trip: Trip? = null
    private val database = FirebaseFirestore.getInstance()
    private lateinit var sharedPref: SharedPreferences
    private lateinit var uId: String


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
        val trainNumber = detailsView.findViewById<TextView>(R.id.details_train_number)
        val imageView = requireActivity().findViewById<ImageView>(R.id.main_options_icon)
        uId = sharedPref.getString(
            getString(R.string.firebase_user_uid),
            "uid could not be retrieved"
        ).toString()
        documentId?.let {
            database.collection("users").document(uId).collection("trips").document(documentId.toString())
                .get().addOnSuccessListener { documentSnapshot ->

                    if (documentSnapshot != null) {
                        trip = documentSnapshot.toObject(Trip::class.java)
                        trainNumber.text = trip?.trainName ?: "Train name empty"
                        val storageReference =
                            FirebaseStorage.getInstance().reference.child("train_pics")
//        val imageReference = storageReference.child("train_pic_1.jpg")
                        var imageReference = storageReference
                        documentId?.let { imageReference = storageReference.child(it) }

                        GlideApp.with(this).load(imageReference).into(imageView)


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