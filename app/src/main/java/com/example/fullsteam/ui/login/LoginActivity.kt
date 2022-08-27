package com.example.fullsteam.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fullsteam.MainActivity
import com.example.fullsteam.R
import com.example.fullsteam.databinding.ActivityLoginBinding
import com.example.fullsteam.firebase.GlideApp
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.time.LocalTime
import kotlin.random.Random

class LoginActivity : AppCompatActivity() {

    private lateinit var signInButton: Button
    private lateinit var backgroundImageView: ImageView
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val storageReference = FirebaseStorage.getInstance().reference.child("background_trainz")
        val imageReference = storageReference.listAll()
        setContentView(R.layout.activity_login)
        signInButton = findViewById(R.id.login)
        backgroundImageView = findViewById(R.id.login_background_image)




        imageReference.addOnSuccessListener {
            val randomPicture = Random(LocalTime.now().nano).nextInt(it.items.size)
            Log.d("random int", randomPicture.toString())
            GlideApp.with(this).load(it.items[randomPicture]).into(backgroundImageView)
        }



        signInButton.setOnClickListener {
            createSignIn()
        }

        if (FirebaseAuth.getInstance().currentUser != null){
            Toast.makeText(this, "${FirebaseAuth.getInstance().currentUser?.displayName} logged in!", Toast.LENGTH_LONG)
                .show()


            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }


    private fun createSignIn() {
        val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

        val signIn =
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers)
                .build()
        signInLauncher.launch(signIn)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        val sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )

        if (result.resultCode == RESULT_OK) {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                with(sharedPref.edit()) {
                    putString(getString(R.string.firebase_user_uid), currentUser.uid)
                    putString(
                        getString(R.string.firebase_user_display_name),
                        currentUser.displayName
                    )
                    putString(
                        getString(R.string.firebase_user_photo_uri),
                        currentUser.photoUrl.toString()
                    )
                    apply()
                }
                Toast.makeText(this, "${currentUser.displayName} logged in!", Toast.LENGTH_LONG)
                    .show()


                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }


        } else {
            Toast.makeText(
                this,
                "Error ${response?.error?.localizedMessage.toString()} occurred.",
                Toast.LENGTH_LONG
            ).show()

        }
    }

    private fun signOut() {
        AuthUI.getInstance().signOut(this).addOnSuccessListener {
            Toast.makeText(this, "Logged out!", Toast.LENGTH_LONG).show()
        }
    }


}