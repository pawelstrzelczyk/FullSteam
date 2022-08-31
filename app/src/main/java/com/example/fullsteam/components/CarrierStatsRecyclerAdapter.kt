package com.example.fullsteam.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fullsteam.R
import com.example.fullsteam.models.StatsCarrier

class CarrierStatsRecyclerAdapter(private val carrierList: ArrayList<StatsCarrier>) :
    RecyclerView.Adapter<CarrierStatsRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.carrier_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.carrierName.text = carrierList[position].name
        holder.carrierTravelCount.text = carrierList[position].tripsNumber.toString()
    }

    override fun getItemCount(): Int {
        return carrierList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val carrierName: TextView
        val carrierTravelCount: TextView

        init {
            carrierTravelCount = itemView.findViewById(R.id.carrier_number_of_travels)
            carrierName = itemView.findViewById(R.id.carrier_name)
        }
    }


}