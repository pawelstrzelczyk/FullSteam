package com.example.fullsteam

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var mainToolbar: Toolbar
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var imageToolbar: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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