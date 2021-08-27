package com.example.hom64chatapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.hom64chatapp.Adapters.CategoryAdapter
import com.example.hom64chatapp.databinding.FragmentUsersAndGroupBinding
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.tab_item.view.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class UsersAndGroupFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding:FragmentUsersAndGroupBinding
     var categoryAdapter:CategoryAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersAndGroupBinding.inflate(layoutInflater)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                customView?.txt_tab_item?.setTextColor(Color.WHITE)
                customView?.tab_layout_root?.background = resources.getDrawable(R.drawable.tab_bacground)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                customView?.txt_tab_item?.setTextColor(Color.BLACK)
                customView?.tab_layout_root?.background = resources.getDrawable(R.drawable.tab_bacground_2)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        categoryAdapter = null
        var fList = ArrayList<Fragment>()
        fList.add(UserLstFragment())
        fList.add(GroupListFragment())
        categoryAdapter = CategoryAdapter(childFragmentManager, fList)
        binding.viewPager.adapter = categoryAdapter


        binding.tabLayout.setupWithViewPager(binding.viewPager)
        setTabs()

        return binding.root
    }

    private fun setTabs() {
        val tabCount = binding.tabLayout.tabCount

        val tabList = arrayListOf<String>("Chats", "Groups")

        for (i in 0 until tabCount){
            val tabView = LayoutInflater.from(context).inflate(R.layout.tab_item, null, false)
            val tab = binding.tabLayout.getTabAt(i)
            tab?.customView = tabView

            tabView.txt_tab_item.text = tabList[i]

            if (i == 0){
                tabView.tab_layout_root.background = resources.getDrawable(R.drawable.tab_bacground)
                tabView.txt_tab_item.setTextColor(Color.WHITE)
            }else{
                tabView.tab_layout_root.background = resources.getDrawable(R.drawable.tab_bacground_2)
                tabView.txt_tab_item.setTextColor(Color.BLACK)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UsersAndGroupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}