package com.example.hom64chatapp

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hom64chatapp.databinding.ActivitySignBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignActivity : AppCompatActivity() {

    lateinit var googleSignInClient: GoogleSignInClient
    private val TAG = "MainActivity"
    var RC_SIGN_IN = 1
    lateinit var auth: FirebaseAuth

    lateinit var binding:ActivitySignBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("828327354580-osbiraq7a6rf0b1hiqcf6mhg0np6jshk.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()


        binding.txtSignIn.setOnClickListener {
            MyAsynTask().execute()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
//                    updateUI(user)
                    Toast.makeText(this, "Muvaffaqiyatli", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
//                    updateUI(null)
                    Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    inner class MyAsynTask : AsyncTask<Void, Void, Void>(){

        override fun onPreExecute() {
            super.onPreExecute()
            binding.progress.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            signIn()
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            binding.progress.visibility = View.GONE
        }
    }
}