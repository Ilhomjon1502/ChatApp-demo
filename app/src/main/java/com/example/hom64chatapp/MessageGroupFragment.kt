package com.example.hom64chatapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.hom64chatapp.Adapters.MessageGroupAdapter
import com.example.hom64chatapp.Models.Group
import com.example.hom64chatapp.Models.Message
import com.example.hom64chatapp.Models.User
import com.example.hom64chatapp.databinding.FragmentMessageGroupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MessageGroupFragment : Fragment() {

    lateinit var binding:FragmentMessageGroupBinding
    lateinit var group:Group

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    lateinit var referenceMessage: DatabaseReference
    lateinit var referenceUsers: DatabaseReference
    lateinit var mesageGroupAdapter: MessageGroupAdapter

    lateinit var messageList : ArrayList<Message>
     var memeberList:ArrayList<User> = ArrayList()
    lateinit var groupList:ArrayList<Group>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessageGroupBinding.inflate(layoutInflater)

        group = arguments?.getSerializable("keyGroup") as Group

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("groups")
        referenceMessage = firebaseDatabase.getReference("messages")
        referenceUsers = firebaseDatabase.getReference("users")

        binding.imageBack.setOnClickListener { findNavController().popBackStack() }

        binding.txtDisplayName.text = group.name

        binding.imagePlus.setOnClickListener {
            val text = binding.edtMessage.text.toString().trim()
            if (text!=""){
                val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
                val date = simpleDateFormat.format(Date())

                val message = Message(firebaseAuth.currentUser?.uid, group.name, text, date)

                referenceMessage.child(referenceMessage.push().key!!).setValue(message)

                binding.edtMessage.text.clear()
                Toast.makeText(context, "Send message", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "Xabar yo'q", Toast.LENGTH_SHORT).show()
            }
        }

        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                groupList = ArrayList()
                val children = snapshot.children
                for (child in children) {
                    val value = child.getValue(Group::class.java)
                    if (value != null) {
                        groupList.add(value)
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Internet bilan bog'liq muammo", Toast.LENGTH_SHORT).show()
            }
        })

        referenceUsers.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                memeberList = ArrayList()
                val children = snapshot.children
                for (child in children) {
                    val value = child.getValue(User::class.java)
                    if (value != null) {
                        memeberList.add(value)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        referenceMessage.addValueEventListener(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList = ArrayList()
                val children = snapshot.children
                for (child in children) {
                    val value = child.getValue(Message::class.java)
                    if (value != null && value.toUid == group.name) {
                        messageList.add(value)
                    }
                }
                mesageGroupAdapter = MessageGroupAdapter(messageList, memeberList, firebaseAuth.currentUser?.uid!!)
                binding.rvMessage.adapter = mesageGroupAdapter
                binding.rvMessage.scrollToPosition(messageList.size-1)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        return binding.root
    }
}