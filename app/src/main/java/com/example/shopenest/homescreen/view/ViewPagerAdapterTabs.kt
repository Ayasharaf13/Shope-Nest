package com.example.shopenest.homescreen.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.shopenest.homescreen.view.AllCategoriesFragment
import com.example.shopenest.homescreen.view.KidsFragment
import com.example.shopenest.homescreen.view.MenFragment
import com.example.shopenest.homescreen.view.WomenFragment


class ViewPagerAdapterTabs (fm:FragmentActivity): FragmentStateAdapter(fm) {


    override fun getItemCount(): Int {

       return 3

    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {

            0 -> WomenFragment()
            1 -> MenFragment()
            2-> KidsFragment()

            else -> AllCategoriesFragment()
        }

    }


}



