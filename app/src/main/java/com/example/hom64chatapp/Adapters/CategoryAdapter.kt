package com.example.hom64chatapp.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.hom64chatapp.GroupListFragment
import com.example.hom64chatapp.UserLstFragment
import java.io.Serializable

class CategoryAdapter(fragmentManager: FragmentManager?, val list: List<Fragment>)
    :FragmentStatePagerAdapter(fragmentManager!!){
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }
}