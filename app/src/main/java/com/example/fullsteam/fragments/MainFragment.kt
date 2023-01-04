package com.example.fullsteam.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.fullsteam.R
import com.example.fullsteam.components.CarrierStatsRecyclerAdapter
import com.example.fullsteam.components.LongHaulStatsRecyclerAdapter
import com.example.fullsteam.models.LongHaulStats
import com.example.fullsteam.models.StatsCarrier
import com.example.fullsteam.models.Trip
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore


class MainFragment : Fragment() {
    private lateinit var usernameText: TextView
    private lateinit var totalDistanceText: TextView
    private lateinit var totalTimeSpentText: TextView
    private lateinit var averageOperatingSpeedText: TextView
    private lateinit var userProfilePictureUri: String
    private lateinit var sharedPref: SharedPreferences
    private lateinit var distanceProgressBar: ProgressBar
    private lateinit var timeProgressBar: ProgressBar
    private lateinit var avgOperatingSpeedProgressBar: ProgressBar
    private lateinit var carriersProgressBar: ProgressBar
    private lateinit var longHaulProgressBar: ProgressBar
    private var database = FirebaseFirestore.getInstance()
    private lateinit var uid: String
    private lateinit var carriersStatsRecyclerView: RecyclerView
    private lateinit var carriersStatsRecyclerAdapter: CarrierStatsRecyclerAdapter
    private lateinit var longHaulStatsRecyclerView: RecyclerView
    private lateinit var longHaulStatsRecyclerAdapter: LongHaulStatsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val imageView = requireActivity().findViewById<ImageView>(R.id.main_options_icon)
        carriersStatsRecyclerView = view.findViewById(R.id.carriers_recycler_view)
        longHaulStatsRecyclerView = view.findViewById(R.id.long_haul_recycler_view)

        uid = sharedPref.getString(
            getString(R.string.firebase_user_uid),
            "uid could not be retrieved"
        ).toString()

        usernameText = view.findViewById(R.id.username_text)
        totalDistanceText = view.findViewById(R.id.total_distance_text)
        totalTimeSpentText = view.findViewById(R.id.total_time_spent_text)
        averageOperatingSpeedText = view.findViewById(R.id.avg_operating_speed_text)
        distanceProgressBar = view.findViewById(R.id.distance_progress_bar)
        timeProgressBar = view.findViewById(R.id.time_progress_bar)
        avgOperatingSpeedProgressBar = view.findViewById(R.id.avg_operating_speed_progress_bar)
        carriersProgressBar = view.findViewById(R.id.carriers_progress_bar)
        longHaulProgressBar = view.findViewById(R.id.long_haul_progress_bar)
        ("Hi, " + sharedPref.getString(
            getString(R.string.firebase_user_display_name),
            "username could not be retrieved"
        )).also { usernameText.text = it }

//        userProfilePictureUri = sharedPref.getString(
//            getString(R.string.firebase_user_photo_uri),
//            "https://d-art.ppstatic.pl/kadry/k/r/1/48/87/60b0e7199f830_o_large.jpg"
//        ).toString()
//        GlideApp.with(this).load(userProfilePictureUri).into(imageView)
        imageView.setImageResource(R.color.ppMain)


        database.collection("users").document(uid).collection("trips").get()
            .addOnSuccessListener { querySnapshot ->
                var totalDistance = 0
                var totalTimeSpent = 0
                var speedSum = 0.0
                var speedSumWithDelays = 0.0
                val carrierList: ArrayList<String> = arrayListOf()
                val carrierListWithCount: ArrayList<StatsCarrier> = arrayListOf()
                val longHaulList: ArrayList<String> = arrayListOf()
                val longHaulListWithCount: ArrayList<LongHaulStats> = arrayListOf()
                val trips: List<Trip> =
                    querySnapshot.documents.map { documentSnapshot -> documentSnapshot.toObject(Trip::class.java)!! }

                trips.forEach {
                    totalDistance += it.kmDistance
                    totalTimeSpent += it.tripTimeInMinutes
                    speedSum += it.avgSpeed
                    speedSumWithDelays += (it.kmDistance / ((it.tripTimeInMinutes + it.delay - it.departureDelay).toDouble() / 60))
                    carrierList.add(it.trainBrand)
                    if (it.trainBrand == "IC") {
                        longHaulList.add(it.trainName)
                    }

                }

                val groupedCarriers = carrierList.groupingBy { it }.eachCount()
                val groupedLongHaul = longHaulList.groupingBy { it }.eachCount()



                speedSum /= trips.size
                speedSumWithDelays /= trips.size
                distanceProgressBar.visibility = View.GONE
                totalDistanceText.visibility = View.VISIBLE
                totalDistanceText.text = totalDistance.toString()

                timeProgressBar.visibility = View.GONE
                totalTimeSpentText.visibility = View.VISIBLE
                totalTimeSpentText.text = totalTimeSpent.toString()

                avgOperatingSpeedProgressBar.visibility = View.GONE
                averageOperatingSpeedText.visibility = View.VISIBLE
                (String.format("%.2f", speedSum) + String.format(
                    " (%.2f)",
                    speedSumWithDelays
                )).also { averageOperatingSpeedText.text = it }
                groupedCarriers.forEach {
                    carrierListWithCount.add(StatsCarrier(it.key, it.value))
                }
                carrierListWithCount.sortByDescending {
                    it.tripsNumber
                }
                groupedLongHaul.forEach {
                    longHaulListWithCount.add(LongHaulStats(it.key, it.value))
                }
                longHaulListWithCount.sortByDescending {
                    it.travelNumber
                }
                carriersProgressBar.visibility = View.GONE
                carriersStatsRecyclerAdapter = CarrierStatsRecyclerAdapter(carrierListWithCount)
                carriersStatsRecyclerView.adapter = carriersStatsRecyclerAdapter

                longHaulProgressBar.visibility = View.GONE
                longHaulStatsRecyclerAdapter = LongHaulStatsRecyclerAdapter(longHaulListWithCount)
                longHaulStatsRecyclerView.adapter = longHaulStatsRecyclerAdapter


            }
        return view
    }

}