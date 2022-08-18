package com.example.fullsteam

import android.os.Bundle
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.fullsteam.fragments.AddTripFormFragment
import com.example.fullsteam.fragments.TripListFragment
import com.example.fullsteam.portalpasazera.PPClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.storage.FirebaseStorage


class MainActivity : AppCompatActivity() {

    private lateinit var startAutoCompleteTextView: AutoCompleteTextView
    private var ppClient: PPClient? = null
    private lateinit var priceEditText: EditText
    private lateinit var mainFragmentView: FragmentContainerView
    private lateinit var addFragment: AddTripFormFragment
    private lateinit var listFragment: TripListFragment
    private lateinit var mainToolbar: Toolbar
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var imageToolbar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //hide navigation bar (bottom, system)
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        WindowInsetsControllerCompat(window, window.decorView).let {
//            it.hide(WindowInsetsCompat.Type.systemBars())
//            it.systemBarsBehavior =
//                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//        }
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        mainToolbar = findViewById(R.id.activity_main_toolbar)
        imageToolbar = findViewById(R.id.main_options_icon)
//        val storageReference = FirebaseStorage.getInstance().reference.child("train_pics")
        val storageReference = FirebaseStorage.getInstance().reference.child("train_pics")
//        val imageReference = storageReference.child("train_pic_1.jpg")
        val imageReference = storageReference.child("train_pic_1.jpg")
        Log.d("dupa", imageReference.toString())



        //GlideApp.with(this).load(imageReference).into(imageToolbar)

        setSupportActionBar(mainToolbar)
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar)
        bottomNavigationView.setupWithNavController(navController)


    }

}