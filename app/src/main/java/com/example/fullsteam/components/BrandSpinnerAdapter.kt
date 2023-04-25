package com.example.fullsteam.components

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.fullsteam.R
import com.example.fullsteam.koleo.brands.Brand


class BrandSpinnerAdapter(context: Context, private val brandList: List<Brand>) :
    ArrayAdapter<Brand>(context, 0, brandList) {
    override fun getCount(): Int {
        return brandList.size
    }

    override fun getItem(p0: Int): Brand {
        return brandList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val brandItem = getItem(position)
        val itemView = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.brand_spinner_item, parent, false)
        brandItem.let {
            val brandText = itemView.findViewById<TextView>(R.id.train_brand_spinner_item_textview)
            brandText.text = brandItem.display_name
            brandText.setTextColor(Color.parseColor(arrayListOf(brandItem.color)[0]))

        }

        return itemView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }


}