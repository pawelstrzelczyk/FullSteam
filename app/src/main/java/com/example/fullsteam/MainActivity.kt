package com.example.fullsteam

import android.content.ContentValues.TAG
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentContainerView
import com.example.fullsteam.fragments.AddTripFormFragment
import com.example.fullsteam.portalpasazera.PPClient
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.NumberFormat


class MainActivity : AppCompatActivity() {

    private lateinit var startAutoCompleteTextView: AutoCompleteTextView
    private var ppClient: PPClient? = null
    private lateinit var priceEditText: EditText
    private lateinit var mainFragmentView: FragmentContainerView
    private lateinit var addFragment: AddTripFormFragment
    private lateinit var mainToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //hide navigation bar (bottom, system)
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        WindowInsetsControllerCompat(window, window.decorView).let {
//            it.hide(WindowInsetsCompat.Type.systemBars())
//            it.systemBarsBehavior =
//                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//        }
        addFragment = AddTripFormFragment()
        setContentView(R.layout.activity_main)
        mainToolbar = findViewById(R.id.activity_main_toolbar)



        supportFragmentManager.beginTransaction().add(R.id.main_fragment_container, addFragment).commit()


//        ppClient = PPClient()
//
//        startAutoCompleteTextView = findViewById(R.id.startAutoCompleteTextView)
//        priceEditText = findViewById(R.id.price_edit_text)
//        val stations: ArrayList<String> = arrayListOf()
//        var ppAdapter = ArrayAdapter(this, R.layout.station_list_item, stations)
//        startAutoCompleteTextView.setAdapter(ppAdapter)
//
//        startAutoCompleteTextView.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                stations.clear()
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                stations.clear()
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//                if (p0.toString().isNotEmpty()) {
//                    Log.d("entered", p0.toString())
//
//                    ppClient!!.getStations(listOf(p0.toString())).observeForever {
//
//                        if (stations.isEmpty()) {
//                            it[0].forEach { station -> stations.add(station.Nazwa) }
//                        }
//
//
//                        ppAdapter.notifyDataSetChanged()
//                    }
//                    Log.d("list", stations.toString())
//                    //stations = ppClient!!.getStations(listOf(p0.toString())).value!!
//
//                    ppAdapter = ArrayAdapter(
//                        applicationContext,
//                        R.layout.station_list_item,
//                        R.id.station_name,
//                        stations
//                    )
//                    startAutoCompleteTextView.setAdapter(ppAdapter)
//
//
//                }
//            }
//        })
//        startAutoCompleteTextView.setOnItemClickListener { adapterView, view, i, l ->
//            Log.d("params", i.toString() + l.toString())
//            Log.d("selected", startAutoCompleteTextView.text.toString())
//        }
//        priceEditText.setRawInputType(Configuration.KEYBOARD_12KEY)
//        val replaceableString = String.format("[%s,.\\s]",
//            NumberFormat.getCurrencyInstance().currency?.symbol ?: ""
//        )
//        priceEditText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            private var current: String = ""
//            override fun afterTextChanged(p0: Editable?) {
//                if (p0.toString() != current) {
//                    priceEditText.removeTextChangedListener(this)
//
//                    val cleanString: String = p0!!.toString().replace(replaceableString, "")
//
//                    val parsed = cleanString.toDouble()
//                    val formatted = NumberFormat.getCurrencyInstance().format((parsed / 100))
//
//                    current = formatted
//                    priceEditText.setText(formatted)
//                    priceEditText.setSelection(formatted.length)
//
//                    priceEditText.addTextChangedListener(this)
//                }
//            }
//
//        })
//        val database = FirebaseFirestore.getInstance()
//        val editTextDatabase = findViewById<EditText>(R.id.editTextDatabase)
//        // Create a new user with a first and last name
//        // Create a new user with a first and last name
//        val user: MutableMap<String, Any> = HashMap()
//        user["first"] = "Ada"
//        user["last"] = "Lovelace"
//
//
//
//        findViewById<Button>(R.id.button).setOnClickListener {
//            user["born"] = editTextDatabase.text.toString()
//            database.collection("trips")
//                .add(user)
//                .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
//                    Log.d(
//                        TAG, "DocumentSnapshot added with ID: " + documentReference.id
//                    )
//                    Log.d(
//                        TAG, "User born: " + editTextDatabase.text.toString()
//                    )
//                })
//                .addOnFailureListener(OnFailureListener { e ->
//                    Log.w(
//                        TAG,
//                        "Error adding document",
//                        e
//                    )
//                })
//
//            database.collection("trips")
//                .whereEqualTo("born", "duuupa")
//                .get()
//                .addOnSuccessListener { result ->
//                    for (document in result) {
//                        Log.d(TAG, "${document.id} => ${document.data}")
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    Log.w(TAG, "Error getting documents.", exception)
//                }
//        }

    }


}