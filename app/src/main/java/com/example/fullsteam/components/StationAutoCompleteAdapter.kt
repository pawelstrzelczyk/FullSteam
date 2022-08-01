package com.example.fullsteam.components

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.fullsteam.R

class StationAutoCompleteAdapter(context: Context, private val stations: ArrayList<String>) :
    ArrayAdapter<String>(context, 0, stations) {
    override fun getItem(p0: Int): String {
        return stations[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("Range")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val stationItem = getItem(position)
        val itemView = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.station_list_item, parent, false)
        val stationText = itemView.findViewById<TextView>(R.id.station_name)
        stationText.text = stationItem

        return itemView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

    override fun getCount(): Int {
        return stations.size
    }

}