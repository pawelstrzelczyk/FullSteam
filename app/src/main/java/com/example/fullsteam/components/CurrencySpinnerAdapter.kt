package com.example.fullsteam.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.fullsteam.R


class CurrencySpinnerAdapter(context: Context, private val currencyList: ArrayList<String>) :
    ArrayAdapter<String>(context, 0, currencyList) {
    override fun getCount(): Int {
        return currencyList.size
    }

    override fun getItem(p0: Int): String {
        return currencyList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val currencyItem = getItem(position)
        val itemView = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.currency_spinner_item, parent, false)
        currencyItem.let {
            val currencyText = itemView.findViewById<TextView>(R.id.trip_currency_option_textview)
            currencyText.text = currencyItem

        }

        return itemView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }


}