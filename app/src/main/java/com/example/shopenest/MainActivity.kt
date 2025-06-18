package com.example.shopenest

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.shopenest.homescreen.viewmodel.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var    homeViewModel:HomeViewModel
    lateinit var navController: NavController
    lateinit var toolbar: Toolbar

    @SuppressLint("LongLogTag")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
         toolbar = findViewById(R.id.toolbar_main)

        navView.setOnItemSelectedListener { item ->
            if (item.itemId == androidx.coordinatorlayout.R.id.accessibility_action_clickable_span) {
                Toast.makeText(this@MainActivity, "Fav", Toast.LENGTH_LONG).show()
                //  navController.navigate(item.getItemId());
            }
            false
        }

        // Find the NavController

        // Find the NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment?
        if (navHostFragment != null) {
            navController = navHostFragment.navController
        }


        // Set up the ActionBar with NavController


        // Set up the ActionBar with NavController
        val appBarConfiguration: AppBarConfiguration =  AppBarConfiguration.Builder(
            R.id.homeFragment, R.id.favFragment, R.id.cartFragment, R.id.profileFragment
        ).build()
       //  setupActionBarWithNavController(this, navController, appBarConfiguration)

        toolbar.title = "Home"
        toolbar.titleMarginBottom = 800

        // getSupportActionBar()?.setTitle("Stringllll"); //string is custom name you want


        setSupportActionBar(findViewById(R.id.toolbar_main))
        // Connect Toolbar with NavController
        toolbar.setupWithNavController(navController,appBarConfiguration)




        // Set up Bottom Navigation Bar with NavController

        // Set up Bottom Navigation Bar with NavController
        setupWithNavController(navView, navController)

        // Enhance user experience by clearing the back stack

        // Enhance user experience by clearing the back stack
        bottomNavItemChangeListener(navView)

        /*  val homeViewModelFactory = HomeViewModelFactory(
              Repository.getInstance(
                  ShoppingClient.getInstance(),
                  ConcreteLocalSource.getInstance(this)
              )
          )

          homeViewModel =
              ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)
          homeViewModel.getBrands()
          Log.i("successBrandsLJ/J", "Brand: ?? ")


          Log.i("successBrandsLJ/Jkkkkkkkkk", "Brand: ??")

         */




        ///////////////////

        //  var brand =   homeViewModel.getBrands()
        /* var brand = homeViewModel.brand.value


        for (bra in brand) {
            Log.i("successBrands", "Brand: ${bra.smart_collections}")
        }



            }

        */
        // Log.i("successBrands", "Brand: $brand")
    }

    private fun bottomNavItemChangeListener(navView: BottomNavigationView) {
        navView.setOnItemSelectedListener { item: MenuItem ->
            if (item.itemId != navView.selectedItemId) {
                navController.popBackStack(item.itemId, false, false)
                navController.navigate(item.itemId)
            }
            true
        }
    }
   /* override fun onResume() {
        super.onResume()


        lifecycleScope.launch(Dispatchers.Main) {

            homeViewModel.brand.collect { brandList ->
                for (bra in brandList) {
                    Log.i("successBrands", "Brand: ${bra.title}")
                }
            }
        }

    }

    */

}