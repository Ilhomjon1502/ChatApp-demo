package com.example.hom64chatapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.hom64chatapp.Adapters.MessageUserAdapter
import com.example.hom64chatapp.Models.Message
import com.example.hom64chatapp.Models.User
import com.example.hom64chatapp.databinding.FragmentMessageUserWriteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MessageUserWriteFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentMessageUserWriteBinding
    lateinit var user:User

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    lateinit var referenceUser: DatabaseReference

    lateinit var messageUserAdapter:MessageUserAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessageUserWriteBinding.inflate(layoutInflater)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("messages")
        referenceUser = firebaseDatabase.getReference("users")

        user = arguments?.getSerializable("keyUser") as User

        Picasso.get().load(user.photoUrl).into(binding.imageUser)
        binding.txtDisplayName.text = user.displayName


        binding.imageBack.setOnClickListener { findNavController().popBackStack() }

        binding.imagePlus.setOnClickListener {
            val m = binding.edtMessage.text.toString().trim()

            if (m!=""){
                    val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
                    val date = simpleDateFormat.format(Date())

                val message = Message(firebaseAuth.currentUser?.uid, user.uid, m, date)
                val key = reference.push().key

                reference.child(key!!).setValue(message)
                binding.edtMessage.text.clear()
                Toast.makeText(context, "Sent $m to ${user.displayName}", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "Avval xabar yozing...", Toast.LENGTH_SHORT).show()
            }
        }

        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val fromUid = firebaseAuth.currentUser?.uid
                val toUid = user.uid
                val list = arrayListOf<Message>()
                val children = snapshot.children
                for (child in children) {
                    val value = child.getValue(Message::class.java)
                    if (value!=null && ((value.fromUid == fromUid && value.toUid == toUid) || (value.fromUid==toUid && value.toUid==fromUid))){
                        list.add(value)
                    }
                }
                messageUserAdapter = MessageUserAdapter(list, fromUid!!)
                binding.rvMessage.adapter = messageUserAdapter
                binding.rvMessage.scrollToPosition(list.size-1)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        referenceUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                for (child in children) {
                    val value = child.getValue(User::class.java)
                    if (value!=null && value.uid == user.uid){
                        user = value
                        if (user.isOnline!!){
                            binding.txtOnline.text = "Online"
                        }else{
                            binding.txtOnline.text = "Online emas"
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MessageUserWriteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}