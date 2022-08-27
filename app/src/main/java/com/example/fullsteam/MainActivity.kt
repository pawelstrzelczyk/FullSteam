package com.example.fullsteam

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.fullsteam.fragments.AddTripFormFragment
import com.example.fullsteam.fragments.TripListFragment
import com.example.fullsteam.portalpasazera.PPClient
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


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
    private lateinit var addPhotoFab: FloatingActionButton

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



        setSupportActionBar(mainToolbar)
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar)
        bottomNavigationView.setupWithNavController(navController)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                true
            }
            R.id.action_logout -> {
                signOut()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }


    }


    private fun signOut() {
        AuthUI.getInstance().signOut(this).addOnSuccessListener {
            Toast.makeText(this, "Logged out!", Toast.LENGTH_LONG).show()
        }
        finish()
    }

}