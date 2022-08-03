package com.example.fullsteam.fragments


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fullsteam.FirebaseHandler
import com.example.fullsteam.databinding.FragmentTripElementBinding
import com.example.fullsteam.models.Trip


class TripListRecyclerViewAdapter(
    private val trips: List<Trip>
) : RecyclerView.Adapter<TripListRecyclerViewAdapter.ViewHolder>() {
    private lateinit var firebaseHandler: FirebaseHandler

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentTripElementBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = trips[position]
        holder.idView.text = item.trainNumber.toString()
        holder.contentView.text = item.trainName
    }

    override fun getItemCount(): Int = trips.size

    inner class ViewHolder(binding: FragmentTripElementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.tripListTrainNumber
        val contentView: TextView = binding.tripListTrainName

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}