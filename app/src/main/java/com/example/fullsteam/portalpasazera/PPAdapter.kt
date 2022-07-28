//package com.example.fullsteam.portalpasazera
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.BaseAdapter
//import android.widget.Filter
//import android.widget.Filterable
//import android.widget.TextView
//import com.example.fullsteam.R
//import com.example.fullsteam.portalpasazera.Station
//
//class PPAdapter : BaseAdapter(), Filterable {
//    private final val maxResults = 6
//    private val context: Context? = null
//    private val stations: List<String> = listOf()
//    private val ppClient = PPClient()
//
//    override fun getCount(): Int {
//        return stations.size
//    }
//
//    override fun getItem(p0: Int): Any {
//        return stations[p0]
//    }
//
//    override fun getItemId(p0: Int): Long {
//        return p0.toLong()
//    }
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        var rowView: View? = null
//        if (convertView == null) {
//            val inflater: LayoutInflater =
//                context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            rowView = inflater.inflate(R.layout.station_list_item, parent, false)
//        }
//        rowView?.findViewById<TextView>(R.id.station_name)!!.text = getItem(position).getStations()
//
//        return rowView
//
//    }
//
//    override fun getFilter(): Filter {
//
//        return object : Filter() {
//            override fun performFiltering(constraint: CharSequence): FilterResults {
//                val filterResults = FilterResults()
//                val stations: List<Station> = ppClient.getStations(constraint.toString())
//
//                // Assign the data to the FilterResults
//                filterResults.values = stations
//                filterResults.count = stations.size
//                return filterResults
//            }
//
//            override fun publishResults(constraint: CharSequence, results: FilterResults) {
//                if (results.count > 0) {
//                    val resultList = results.values as List<Station?>
//                    notifyDataSetChanged()
//                } else {
//                    notifyDataSetInvalidated()
//                }
//            }
//        }
//
//    }
//}