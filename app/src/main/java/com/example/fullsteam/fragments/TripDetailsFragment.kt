package com.example.fullsteam.fragments

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.fullsteam.GlideApp
import com.example.fullsteam.R
import com.example.fullsteam.models.Trip
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

private const val DOCUMENT_ID = "documentId"

class TripDetailsFragment : Fragment() {
    private var documentId: String? = null
    private var trip: Trip? = null
    private val database = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            documentId = it.getString(DOCUMENT_ID)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val detailsView = inflater.inflate(R.layout.fragment_trip_details, container, false)
        val trainNumber = detailsView.findViewById<TextView>(R.id.details_train_number)
        val imageView = requireActivity().findViewById<ImageView>(R.id.main_options_icon)
        documentId?.let {
            database.collection("trips").document(documentId.toString())
                .get().addOnSuccessListener {

                    if (it != null) {
                        trip = it.toObject(Trip::class.java)
                        trainNumber.text = trip?.trainName ?: "Train name empty"
                        val storageReference =
                            FirebaseStorage.getInstance().reference.child("train_pics")
//        val imageReference = storageReference.child("train_pic_1.jpg")
                        val imageReference = storageReference.child("train_pic_1.jpg")
                        if (trip?.trainName == "GWAREK") {
                            GlideApp.with(this).load(imageReference).into(imageView)
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