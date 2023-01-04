package com.example.fullsteam

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.fullsteam.firebase.GlideApp
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var mainToolbar: Toolbar
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var imageToolbar: ImageView
    private lateinit var uid: String
    private lateinit var sharedPref: SharedPreferences
    private lateinit var userProfilePictureUri: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        mainToolbar = findViewById(R.id.activity_main_toolbar)
        imageToolbar = findViewById(R.id.main_options_icon)

        uid = sharedPref.getString(
            getString(R.string.firebase_user_uid),
            "uid could not be retrieved"
        ).toString()

//        userProfilePictureUri = sharedPref.getString(
//            getString(R.string.firebase_user_photo_uri),
//            "https://d-art.ppstatic.pl/kadry/k/r/1/48/87/60b0e7199f830_o_large.jpg"
//        ).toString()

//        GlideApp.with(this).load(userProfilePictureUri).into(imageToolbar)
        imageToolbar.setImageResource(R.color.ppMain)


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