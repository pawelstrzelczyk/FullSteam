package com.example.fullsteam.fragments

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.*
import com.example.fullsteam.R
import com.example.fullsteam.firebase.FirebaseHandler
import com.example.fullsteam.models.Trip
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class TripListFragment : Fragment() {
    private lateinit var firebaseHandler: FirebaseHandler
    private lateinit var trips: ArrayList<Trip>
    private lateinit var tripAdapter: TripListRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPref: SharedPreferences
    private lateinit var uId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseHandler = FirebaseHandler()
        trips = arrayListOf()

        sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        uId = sharedPref.getString(
            getString(R.string.firebase_user_uid),
            "uid could not be retrieved"
        ).toString()
        val view = inflater.inflate(R.layout.fragment_trip_list, container, false)
        val query: Query = FirebaseFirestore.getInstance()
            .collection("users").document(uId).collection("trips")
            .orderBy("dateTime", Query.Direction.DESCENDING)
        val config = PagingConfig(20, 10, false)

        val options: FirestorePagingOptions<Trip> = FirestorePagingOptions.Builder<Trip>()
            .setLifecycleOwner(this)
            .setQuery(query, config, Trip::class.java)
            .build()
        recyclerView = view.findViewById(R.id.trip_list_recycler_view)

        tripAdapter = TripListRecyclerViewAdapter(options)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            recyclerView.layoutManager = LinearLayoutManager(context)
        }
        recyclerView.adapter = tripAdapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        Log.d("trips size", trips.size.toString())
        tripAdapter.setOnItemClickListener(object :
            TripListRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int) {
                val id: String = documentSnapshot.id
                Toast.makeText(requireContext(), "$id document clicked", Toast.LENGTH_LONG).show()
                val bundle = Bundle()
                bundle.putString("documentId", documentSnapshot.id)
                view.findNavController()
                    .navigate(R.id.action_tripListFragment_to_tripDetailsFragment, bundle)
            }
        })

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                tripAdapter.deleteTrip(viewHolder.absoluteAdapterPosition)
                Snackbar.make(view, "Trip deleted!", Snackbar.LENGTH_LONG).show()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.swipeToDelete
                        )
                    )
                    .addActionIcon(R.drawable.ic_baseline_delete_forever_24)
                    .create()
                    .decorate();

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

        }).attachToRecyclerView(recyclerView)


        return view
    }


//
//    override fun onStart() {
//        super.onStart()
//        recyclerView.recycledViewPool.clear()
////        tripAdapter.notifyDataSetChanged()
//        tripAdapter.startListening()
//
//    }
//
//    override fun onStop() {
//        super.onStop()
//        recyclerView.recycledViewPool.clear()
//        tripAdapter.stopListening()
//    }


}