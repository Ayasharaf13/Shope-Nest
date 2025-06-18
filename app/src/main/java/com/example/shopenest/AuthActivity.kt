package com.example.shopenest

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.viewpager2.widget.ViewPager2
import com.example.shopenest.onboardingscreen.ViewPagerAdapterAuth

class AuthActivity : AppCompatActivity() {

  //  lateinit var navController: NavController
   lateinit var viewPagerAdapterAuth: ViewPagerAdapterAuth
    lateinit var viewPagerauth :ViewPager2




    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        viewPagerauth = findViewById(R.id.viewpagerAuth)
        viewPagerAdapterAuth = ViewPagerAdapterAuth(this)
        viewPagerauth.adapter = viewPagerAdapterAuth

        // Find the NavController
         /*  val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_auth) as NavHostFragment?
        if (navHostFragment != null) {
            navController = navHostFragment.navController
        }

          */







        // Set up the ActionBar with NavController
     /*   val appBarConfiguration: AppBarConfiguration =  AppBarConfiguration.Builder(
            R.id.onboardingFragmentOne, R.id.onboardingFragmentTwo, R.id.onboardingFragmentThree
         //   R.id.welcomeFragment

        ).build()

      */

    }




    fun goToNextPage(currentPosition: Int) {
        if (currentPosition < 2) {  // Since you have 3 pages: 0, 1, 2
            viewPagerauth.currentItem = currentPosition + 1
        }
        }


}

