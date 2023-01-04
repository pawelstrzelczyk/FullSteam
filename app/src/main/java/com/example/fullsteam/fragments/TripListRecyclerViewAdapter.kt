package com.example.fullsteam.fragments


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.example.fullsteam.R
import com.example.fullsteam.models.Trip
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.DocumentSnapshot


class TripListRecyclerViewAdapter(
    options: FirestorePagingOptions<Trip>
) : FirestorePagingAdapter<Trip, TripListRecyclerViewAdapter.TripHolder>(options) {
    private lateinit var listener: OnItemClickListener

    override fun onBindViewHolder(holder: TripHolder, position: Int, model: Trip) {
        var backgroundColor = Color.White.hashCode()
        var strokeColor = Color.White.hashCode()
        holder.trainNameTextView.text = model.trainName
        holder.trainNumberTextView.text = model.trainNumber.toString()
        holder.trainCarrierTextView.text = model.carrier
        holder.trainBrandTextView.text = model.trainBrand
        holder.tripStartStation.text = model.startStation
        holder.tripEndStation.text = model.endStation
        holder.tripDate.text = model.dateTime

        if (model.isPKM) {
            backgroundColor = Color(142, 169, 219, 255).hashCode()
        } else if (model.isSleepingCar) {
            backgroundColor = Color(105, 194, 125, 255).hashCode()

        }
        when {
            model.delay <= 30 -> {
                strokeColor = backgroundColor
            }
            model.delay in 31..60 -> {
                strokeColor = Color(245, 170, 78, 255).hashCode()
            }
            model.delay in 61..120 -> {
                strokeColor = Color(252, 66, 66, 255).hashCode()
            }
            model.delay > 120 -> {
                strokeColor = Color(0, 0, 0, 255).hashCode()
                holder.tripDate.setTextColor(Color.White.hashCode())
            }
        }
        holder.tripListCardView.setCardBackgroundColor(backgroundColor)
        holder.delayBackground.setBackgroundColor(strokeColor)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_trip_element, parent, false)
        return TripHolder(view)
    }

    inner class TripHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val trainNameTextView: TextView
        val trainNumberTextView: TextView
        val trainCarrierTextView: TextView
        val trainBrandTextView: TextView
        val tripStartStation: TextView
        val tripEndStation: TextView
        val tripDate: TextView
        val tripListCardView: MaterialCardView
        val delayBackground: LinearLayout

        init {
            itemView.setOnClickListener {

                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    snapshot()[position]?.let { it1 -> listener.onItemClick(it1, position) }

                }
            }

            trainNameTextView = itemView.findViewById(R.id.trip_list_train_name)
            trainNumberTextView = itemView.findViewById(R.id.trip_list_train_number)
            trainCarrierTextView = itemView.findViewById(R.id.trip_list_train_carrier)
            trainBrandTextView = itemView.findViewById(R.id.trip_list_train_brand)
            tripStartStation = itemView.findViewById(R.id.trip_list_start_station)
            tripEndStation = itemView.findViewById(R.id.trip_list_end_station)
            tripDate = itemView.findViewById(R.id.trip_list_trip_date)
            tripListCardView = itemView.findViewById(R.id.trip_list_card_view)
            delayBackground = itemView.findViewById(R.id.delay_background)

        }

    }

    fun deleteTrip(position: Int, tripListRecyclerViewAdapter: TripListRecyclerViewAdapter) {
        snapshot()[position]?.reference?.delete()
        tripListRecyclerViewAdapter.notifyDataSetChanged()
        tripListRecyclerViewAdapter.refresh()
    }

    interface OnItemClickListener {
        fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int)
    }


    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

}


