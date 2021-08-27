package com.example.hom64chatapp

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.hom64chatapp.Models.Group
import com.example.hom64chatapp.databinding.FragmentGroupListBinding
import com.example.hom64chatapp.databinding.ItemDialogAddGroupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GroupListFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding:FragmentGroupListBinding
    var groupList = ArrayList<Group>()

    private val TAG = "RealActivity"
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var referense: DatabaseReference //pathlar bilan ishlashga yordam beradi

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGroupListBinding.inflate(layoutInflater)

        firebaseAuth = FirebaseAuth.getInstance()

        val currentUser = firebaseAuth.currentUser
        firebaseDatabase = FirebaseDatabase.getInstance()
        referense = firebaseDatabase.getReference("groups")

        referense.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                groupList.clear()
                val children = snapshot.children
                for (child in children) {
                    val value = child.getValue(Group::class.java)
                    if (value != null) {
                        value.uid = snapshot.key
                        groupList.add(value)
                    }
                }
                val nameGList = ArrayList<String>()
                for (group in groupList) {
                    if (group!=null) {
                        nameGList.add(group.name.toString())
                    }
                }
                val gAdapter = ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1, nameGList)
                binding.gList.adapter = gAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        binding.btnAddGroup.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context).create()
            val itemDialogAddGroupBinding = ItemDialogAddGroupBinding.inflate(layoutInflater)
            alertDialog.setView(itemDialogAddGroupBinding.root)
            itemDialogAddGroupBinding.btnSave.setOnClickListener {
                val name = itemDialogAddGroupBinding.edtNameGroup.text.toString().trim()
                if (name!=""){
                    val key = referense.push().key
                    referense.child(key!!).setValue(Group(name))
                    Toast.makeText(context, "Group added", Toast.LENGTH_SHORT).show()
                    alertDialog.cancel()
                }else{
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
            }
            alertDialog.show()
        }

        binding.gList.setOnItemClickListener(object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                findNavController().navigate(R.id.messageGroupFragment, bundleOf("keyGroup" to groupList[p2]))
            }
        })

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GroupListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}