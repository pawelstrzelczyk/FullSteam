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
import com.example.fullsteam.fragments.TripListFragment
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
    private lateinit var listFragment: TripListFragment
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
        listFragment = TripListFragment()
        setContentView(R.layout.activity_main)
        mainToolbar = findViewById(R.id.activity_main_toolbar)



        supportFragmentManager.beginTransaction().add(R.id.main_fragment_container, listFragment)
            .commit()


    }


}