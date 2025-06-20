package com.example.shopenest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.shopenest.onboardingscreen.ViewPagerAdapterAuth

class OnboardingActivity : AppCompatActivity() {

      lateinit var viewPagerAdapterAuth: ViewPagerAdapterAuth
     lateinit var viewPagerauth : ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

         viewPagerauth = findViewById(R.id.viewpagerAuth)
         viewPagerAdapterAuth = ViewPagerAdapterAuth(this)
          viewPagerauth.adapter = viewPagerAdapterAuth
    }

     fun goToNextPage(currentPosition: Int) {
     if (currentPosition < 2) {  // Since you have 3 pages: 0, 1, 2
         viewPagerauth.currentItem = currentPosition + 1
     }

     }
}