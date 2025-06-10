package com.example.shopenest.homescreen.view


import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.shopenest.R
import com.example.shopenest.db.ConcreteLocalSource
import com.example.shopenest.homescreen.GenericAdapterSliderImage

import com.example.shopenest.homescreen.ViewPagerAdapterTabs
import com.example.shopenest.homescreen.viewmodel.HomeViewModel
import com.example.shopenest.homescreen.viewmodel.HomeViewModelFactory
import com.example.shopenest.model.Repository
import com.example.shopenest.network.ShoppingClient
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.*
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator


class HomeFragment : Fragment() {

    lateinit var images: MutableList<Int>
    lateinit var homeViewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var adapter: BrandAdapter
    lateinit var tabsAdapter:ViewPagerAdapterTabs
    lateinit var tablLayout: TabLayout
    private val tabTextList = arrayOf( "Women", "Man","Kid")
    private lateinit var search:EditText
    private lateinit var viewPagertabs:ViewPager2
    private lateinit var viewPagerAds: ViewPager2
    private lateinit var txtSeeAllProducts:TextView

   // private lateinit var tabLayoutIndicator:TabLayout

    var selectedTabPosition = 0


    var slidingJob: Job? = null // Keep track of the coroutine job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        tabsAdapter = ViewPagerAdapterTabs(requireActivity())
// Direct initialization at the point of declaration
        images = mutableListOf()

        //  Handler(Looper.getMainLooper())
        //  slideModels = mutableListOf<SlideModel>()
        images.add(R.drawable.discount1)
        images.add(R.drawable.discount2)
        images.add(R.drawable.discount3)
        images.add(R.drawable.discount4)
        images.add(R.drawable.discount5)

        images.addAll(images)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(com.example.shopenest.R.layout.fragment_home, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
// Start the automatic slideshow when the activity or view is created

      //   tabLayoutIndicator = view.findViewById(R.id.tabLayoutIndicator)



    /*  var  gridView: GridView = view.findViewById(R.id.gridProduct);

        // create a object of myBaseAdapter
       var  baseAdapter: GridProductAdapter =  GridProductAdapter( images);
        gridView.setAdapter(baseAdapter);

     */






     viewPagerAds =view. findViewById (R.id.viewpagerAds)
       search = view.findViewById(R.id.searchText)

        txtSeeAllProducts = view.findViewById(R.id.txtSeeMoreProduct)
      tablLayout = view.findViewById(R.id.tabLayout03)

        viewPagertabs = view.findViewById(R.id.viewPagerCategory)




        //   var  gridProducts :GridView= view.findViewById(R.id.gridProduct)

        var recyclerBrands :RecyclerView = view.findViewById(R.id.RecyclerViewBrands)

        adapter = BrandAdapter(requireView())

     //   recyclerBrand.layoutManager = LinearLayoutManager(requireContext(),HorizontalScrollView)

        recyclerBrands.setLayoutManager(
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )



      //
        //  recyclerBrands.visibility =View.INVISIBLE


   homeViewModelFactory = HomeViewModelFactory(
       Repository.getInstance(
           ShoppingClient.getInstance(),
           ConcreteLocalSource.getInstance(requireContext())
       ))


   homeViewModel =
       ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)

        homeViewModel.getBrands()
        lifecycleScope.launch(Dispatchers.Main) {

            homeViewModel.brand.collect { brandList ->

                recyclerBrands.adapter = adapter
                adapter.submitList(brandList)


                }
            }


        val adapter = GenericAdapterSliderImage<Int>(images,requireContext(),  bind = { product, view, position ->

            view.findViewById<ImageView>(R.id.imageView).setImageResource(images[position])
        })
        viewPagerAds.adapter = adapter

        startSliding()

       // TabLayoutMediator(tabLayoutIndicator, viewPagerAds) { tab, position->


       // }.attach()




        viewPagerAds.setOnClickListener{

            // initializing clip board manager on below line.
            val clipboardManager =
                requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

            // initializing clip data on below line.
            val clipData = ClipData.newPlainText(
                "Copy if you want",
                "Clip Data"
            ) .apply {
                // on below line adding description
                description.extras = PersistableBundle().apply {
                    // only available for Android13 or higher
                    putBoolean(ClipDescription.MIMETYPE_TEXT_PLAIN, true)
                    // use raw string for older versions
                    // android.content.extra.IS_SENSITIVE
                }
            }

            // on below line setting primary clip for clip board manager.
            clipboardManager.setPrimaryClip(clipData)

            // displaying toast message as text copied to clip board.
           Toast.makeText(requireContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show()
        }
        // viewPager.setPageTransformer(ViewPagerHorizontalFlipTransformer())



       // viewPager.setPageTransformer(SimplePageTransformer(SimplePageTransformer.CUBE_INSIDE));


      viewPagertabs.adapter = tabsAdapter

       // tableLayout.setup//(viewPagertabs);
        TabLayoutMediator(tablLayout, viewPagertabs) { tab, pos ->
            tab.text= tabTextList[pos]



        }.attach()

        setTabItemMargin(tablLayout, 30)


        txtSeeAllProducts.setOnClickListener{

         val  action : NavDirections =  HomeFragmentDirections.actionHomeFragmentToSeeAllFragment(position = selectedTabPosition , `isItFromTheBrand` = false )//SettingFragmentDirections.actionSettingFragment2ToSplashFragment();
            Navigation.findNavController(view).navigate(action);


        }
        tablLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

             override fun onTabSelected(tab: TabLayout.Tab?) {
                   viewPagertabs.isUserInputEnabled = false
                 val position = tab?.position ?: return
                 selectedTabPosition = tab?.position ?: 0

                 Log.i("TabClickTest", "Tab $position clicked!")

                 tab.view.setBackgroundResource(R.drawable.selecttab)
                 tab.view.setBackgroundResource(R.drawable.tabstyle)

                // viewPagertabs.currentItem = position // Update ViewPager2 to the clicked tab
             }

             override fun onTabUnselected(tab: TabLayout.Tab?) {
                 tab?.let {
                     Log.i("TabClick", "Tab UNat position ${it.position} reselected")
                     // Handle tab reselection if needed

                     tab.view.setBackgroundResource(R.drawable.selecttab)
                     tab.view.setBackgroundResource(R.drawable.tabstyle)
                 }
             }

             override fun onTabReselected(tab: TabLayout.Tab?) {
                 tab?.let {
                     Log.i("TabClick", "Tab at position ${it.position} reselected")
                     // Handle tab reselection if needed

                 }

             }


         })



// Disable swiping in ViewPager2
        // Handle page change callbacks (for swipe)
        viewPagertabs.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                      //  viewPagertabs.isUserInputEnabled = true
                    Log.i("PageChange", "Page $position selected via swipe")
                    // Ensure the correct tab is selected when swiping

                     tablLayout.getTabAt(position)?.select()
                }
            })



        tablLayout.tabRippleColor = ColorStateList.valueOf(Color.parseColor("#FF03DAC5"))

        // implement the TextWatcher callback listener
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Trigger filtering here
                homeViewModel.filterBrands(s.toString())
                Log.i("filtterbrands",s.toString())

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


    }

    @SuppressLint("LongLogTag")
    private fun setTabItemMargin(tabLayout: TabLayout, marginEnd: Int = 20) {
        val tabs = tabLayout.getChildAt(0) as ViewGroup

        for (i in 0 until tabs.childCount) {
            val tab = tabs.getChildAt(i)

            val lp = tab.layoutParams as LinearLayout.LayoutParams
            lp.marginEnd = marginEnd
            tab.layoutParams = lp

        }

        tabLayout.requestLayout() // Request layout update for TabLayout


    }



    override fun onDestroy() {
        super.onDestroy()
        slidingJob?.cancel()


    }

    private var currentIndex = 0


    private fun startSliding() {
        // Cancel any existing job to prevent multiple slides running concurrently
        slidingJob?.cancel()

        // Launch a coroutine in the viewLifecycleOwner's lifecycle scope
        slidingJob = lifecycleScope.launch {
            while (isActive) { // Ensure the coroutine stops if the lifecycle is destroyed

                if (currentIndex >= images.size) {

                    currentIndex = 0 // Reset to the first slide
                    viewPagerAds.setCurrentItem(
                        currentIndex,
                        false
                    ) // Instantly reset to the first slide

                } else {
                    viewPagerAds.setCurrentItem(
                        currentIndex,
                        true
                    ) // Smoothly transition to the next slide
                }
                currentIndex++

                delay(2000) // Delay for 2 seconds between slides

            }
        }
        }


companion object {

 @JvmStatic
 fun newInstance(param1: String, param2: String) =
     HomeFragment().apply {
         arguments = Bundle().apply {

         }

     }
}




}