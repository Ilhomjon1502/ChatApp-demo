package com.example.hom64chatapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.hom64chatapp.Adapters.RvUserAdapter
import com.example.hom64chatapp.Models.Message
import com.example.hom64chatapp.Models.User
import com.example.hom64chatapp.databinding.FragmentUserLstBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class UserLstFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding:FragmentUserLstBinding

    private val TAG = "RealActivity"
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var referense: DatabaseReference //pathlar bilan ishlashga yordam beradi
    lateinit var referenseMessage: DatabaseReference //pathlar bilan ishlashga yordam beradi
    var list = ArrayList<User>()
    var messageList = ArrayList<Message>()
    lateinit var userAdapter:RvUserAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserLstBinding.inflate(layoutInflater)

        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        firebaseDatabase = FirebaseDatabase.getInstance()
        referense = firebaseDatabase.getReference("users")
        referenseMessage = firebaseDatabase.getReference("messages")

        val email = currentUser?.email
        val displayName = currentUser?.displayName
        val phoneNumber = currentUser?.phoneNumber
        val photoUri = currentUser?.photoUrl
        val uid = currentUser?.uid

        UserOnileOfline.user = User(uid, email, displayName, phoneNumber, photoUri.toString(), true)

        referenseMessage.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                messageList.clear()
                for (child in children) {
                    val value = child.getValue(Message::class.java)
                    if (value != null) {
                        messageList.add(value)
                    }
                }
                referense.addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        list.clear()
                        val filterList = arrayListOf<User>()
                        val children = snapshot.children
                        for (child in children) {
                            val value = child.getValue(User::class.java)
                            if (value != null && uid != value.uid) {
                                list.add(value)
                            }
                            if (value!=null && value.uid==uid){
                                filterList.add(value)
                            }
                        }
                        if (filterList.isEmpty()){
                            referense.child(uid!!).setValue(UserOnileOfline.user)
                        }

                        userAdapter = RvUserAdapter(list, messageList, firebaseAuth.currentUser?.uid!!, object :RvUserAdapter.RvClick{
                            override fun onClick(user: User) {
                                findNavController().navigate(R.id.messageUserWriteFragment, bundleOf("keyUser" to  user))
                            }
                        })

                        binding.rvUser.adapter = userAdapter
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        return binding.root
    }



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserLstFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}