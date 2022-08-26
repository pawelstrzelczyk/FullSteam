package com.example.fullsteam.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.fullsteam.R
import com.example.fullsteam.firebase.GlideApp


class MainFragment : Fragment() {
    private lateinit var usernameText: TextView
    private lateinit var userUIDText: TextView
    private lateinit var userProfilePictureUri: String
    private lateinit var sharedPref: SharedPreferences

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

        usernameText = view.findViewById(R.id.username_text)
        userUIDText = view.findViewById(R.id.user_uid_text)
        usernameText.text = sharedPref.getString(
            getString(R.string.firebase_user_display_name),
            "username could not be retrieved"
        )
        userUIDText.text = sharedPref.getString(
            getString(R.string.firebase_user_uid),
            "uid could not be retrieved"
        )
        userProfilePictureUri = sharedPref.getString(
            getString(R.string.firebase_user_photo_uri),
            "https://d-art.ppstatic.pl/kadry/k/r/1/48/87/60b0e7199f830_o_large.jpg"
        ).toString()
        GlideApp.with(this).load(userProfilePictureUri).into(imageView)
        return view
    }

}