package com.example.hom64chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var firebaseAuth:FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var referenseUser:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        referenseUser = firebaseDatabase.getReference("users")

    }

    //bu metod navigation shu activityda boshlanadi va bizga homeFragmentni ochib beradi
    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.my_navigation_host).navigateUp()
    }

    override fun onPause() {
        super.onPause()
        UserOnileOfline.user.isOnline = false
        UserOnileOfline.user.lastTime = SimpleDateFormat("dd.MM.yyyy HH:mm").format(Date()).toString()
        referenseUser.child(firebaseAuth.currentUser?.uid!!).setValue(UserOnileOfline.user)
    }

    override fun onResume() {
        super.onResume()
        UserOnileOfline.user.isOnline = true
        referenseUser.child(firebaseAuth.currentUser?.uid!!).setValue(UserOnileOfline.user)
    }
}