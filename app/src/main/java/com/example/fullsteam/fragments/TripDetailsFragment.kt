package com.example.fullsteam.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.fullsteam.R

private const val DOCUMENT_ID = "documentId"

class TripDetailsFragment : Fragment() {
    private var documentId: String? = null


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
        trainNumber.text = documentId
        return detailsView
    }

}