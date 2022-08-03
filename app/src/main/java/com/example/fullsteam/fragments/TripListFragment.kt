package com.example.fullsteam.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fullsteam.FirebaseHandler
import com.example.fullsteam.R
import com.example.fullsteam.models.Trip


class TripListFragment : Fragment() {
    private lateinit var firebaseHandler: FirebaseHandler
    private lateinit var trips: ArrayList<Trip>

    override fun onCreate(savedInstanceState: Bundle?) {
        firebaseHandler = FirebaseHandler()
        trips = arrayListOf()
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trip_list, container, false)

        Log.d("trips size", trips.size.toString())
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = TripListRecyclerViewAdapter(trips)
            }
        }
        return view
    }


}