package com.example.shopenest.onboardingscreen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.shopenest.onboardingscreen.view.OnboardingFragmentOne
import com.example.shopenest.onboardingscreen.view.OnboardingFragmentThree
import com.example.shopenest.onboardingscreen.view.OnboardingFragmentTwo


class ViewPagerAdapterAuth(fm: FragmentActivity) : FragmentStateAdapter(fm) {


    override fun getItemCount(): Int {

        return 3

    }


    override fun createFragment(position: Int): Fragment {

        return when (position) {

            0 -> OnboardingFragmentOne()

            1 -> OnboardingFragmentTwo()
            2 -> OnboardingFragmentThree()


            else -> OnboardingFragmentOne()
        }

    }


}
