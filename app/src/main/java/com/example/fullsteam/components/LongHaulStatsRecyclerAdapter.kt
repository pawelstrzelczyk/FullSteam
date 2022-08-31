package com.example.fullsteam.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fullsteam.R
import com.example.fullsteam.models.LongHaulStats

class LongHaulStatsRecyclerAdapter(private val longHaulList: ArrayList<LongHaulStats>) :
    RecyclerView.Adapter<LongHaulStatsRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.long_haul_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.trainName.text = longHaulList[position].name
        holder.longHaulNumberOfTravels.text = longHaulList[position].travelNumber.toString()
    }

    override fun getItemCount(): Int {
        return longHaulList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val trainName: TextView
        val longHaulNumberOfTravels: TextView

        init {
            trainName = itemView.findViewById(R.id.long_haul_train_name)
            longHaulNumberOfTravels = itemView.findViewById(R.id.long_haul_train_number_of_travels)
        }
    }


}