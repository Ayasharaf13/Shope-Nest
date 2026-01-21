package com.example.shopenest.homescreen.view


import android.annotation.SuppressLint
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.content.res.ColorStateList
import android.graphics.Color
import android.icu.util.Currency
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.shopenest.MainActivity
import com.example.shopenest.R
import com.example.shopenest.db.ConcreteLocalSource
import com.example.shopenest.homescreen.GenericAdapterSliderImage
import com.example.shopenest.homescreen.viewmodel.HomeViewModel
import com.example.shopenest.homescreen.viewmodel.HomeViewModelFactory
import com.example.shopenest.model.Repository
import com.example.shopenest.network.ShoppingClient
import com.example.shopenest.utilities.GoogleAuthHelper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import java.util.*


class HomeFragment : Fragment() {

    lateinit var images: MutableList<Int>
    lateinit var homeViewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var adapter: BrandAdapter
    lateinit var tabsAdapter: ViewPagerAdapterTabs
    lateinit var tablLayout: TabLayout
    private val tabTextList = arrayOf("Women", "Man", "Kid")
    private lateinit var viewPagertabs: ViewPager2
    private lateinit var viewPagerAds: ViewPager2
    private lateinit var txtSeeAllProducts: TextView
    private lateinit var buttonLogout: Button
    private lateinit var googleAuthHelper: GoogleAuthHelper
    var code: String? = null

    private lateinit var pref: SharedPreferences
    lateinit var auth: FirebaseAuth

    // private lateinit var tabLayoutIndicator:TabLayout

    var selectedTabPosition = 0


    var slidingJob: Job? = null // Keep track of the coroutine job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        tabsAdapter = ViewPagerAdapterTabs(requireActivity())

        images = mutableListOf()

        //  Handler(Looper.getMainLooper())
        //  slideModels = mutableListOf<SlideModel>()
        images.add(R.drawable.discount1)
        images.add(R.drawable.discount2)
        images.add(R.drawable.discount3)
        images.add(R.drawable.discount4)
        images.add(R.drawable.discount5)

        images.addAll(images)




        homeViewModelFactory = HomeViewModelFactory(
            Repository.getInstance(
                ShoppingClient.getInstance(),
                ConcreteLocalSource.getInstance(requireContext())
            )
        )

        homeViewModel =
            ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)



        suspend fun safeCall(block: suspend () -> Unit) {
            repeat(3) { attempt ->
                try {
                    block()
                    return // success
                } catch (e: Exception) {
                    if (attempt == 2) throw e
                    delay(500) // backoff
                }
            }
        }

        lifecycleScope.launch {
            safeCall { homeViewModel.getBrands() }

            safeCall { homeViewModel.getDiscount() }
        }


        lifecycleScope.launch {

            homeViewModel.getBrands()

            delay(500)

            homeViewModel.getDiscount()


        }



        lifecycleScope.launch {

            homeViewModel.discount.collect { code ->
                Log.d("DiscountCode", "Discount = $code")

                this@HomeFragment.code = code


            }

        }


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

        auth = FirebaseAuth.getInstance()
        //  buttonLogout = view.findViewById(R.id.buttonLogout)

        googleAuthHelper = GoogleAuthHelper(
            context = requireContext(),
            lifecycleOwner = requireActivity(),

            onSuccess = { user ->
                // ✅ User signed in successfully
                Log.d("GoogleAuth", "Welcome: ${user?.email}")
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()

            },

            onError = { errorMessage ->
                // ❌ Sign-in failed
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }


        )




        viewPagerAds = view.findViewById(R.id.viewpagerAds)
        tablLayout = view.findViewById(R.id.tabLayout03)
        viewPagertabs = view.findViewById(R.id.viewPagerCategory)


        var recyclerBrands: RecyclerView = view.findViewById(R.id.RecyclerViewBrands)

        adapter = BrandAdapter(requireView())



        recyclerBrands.setLayoutManager(
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )



        lifecycleScope.launch(Dispatchers.Main) {

            homeViewModel.brand.collect { brandList ->

                recyclerBrands.adapter = adapter
                adapter.submitList(brandList)


            }
        }


        // Here you can trigger your ViewModel logic

        viewLifecycleOwner.lifecycleScope.launch {

            delay(2000)

            val adapter = GenericAdapterSliderImage<Int>(
                code,
                images,
                requireContext(),
                bind = { product, view, position ->

                    view.findViewById<ImageView>(R.id.imageView).setImageResource(images[position])


                })

            viewPagerAds.adapter = adapter

            startSliding()


        }


        viewPagerAds.setOnClickListener {

            // initializing clip board manager on below line.
            val clipboardManager =
                requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

            // initializing clip data on below line.
            val clipData = ClipData.newPlainText(
                "Copy if you want",
                "Clip Data"
            ).apply {
                // on below line adding description
                description.extras = PersistableBundle().apply {
                    // only available for Android13 or higher
                    putBoolean(ClipDescription.MIMETYPE_TEXT_PLAIN, true)

                }
            }

            // on below line setting primary clip for clip board manager.
            clipboardManager.setPrimaryClip(clipData)

            // displaying toast message as text copied to clip board.
            Toast.makeText(requireContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show()
        }



        viewPagertabs.adapter = tabsAdapter

        // tableLayout.setup//(viewPagertabs);
        TabLayoutMediator(tablLayout, viewPagertabs) { tab, pos ->
            tab.text = tabTextList[pos]


        }.attach()

        setTabItemMargin(tablLayout, 30)

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

                    tablLayout.getTabAt(position)?.select()
                }
            })



        tablLayout.tabRippleColor = ColorStateList.valueOf(Color.parseColor("#FF03DAC5"))


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


    private var currentIndex = 0


    private fun startSliding() {
        // Cancel any existing job to prevent multiple slides running concurrently
        slidingJob?.cancel()

        // Launch a coroutine in the viewLifecycleOwner's lifecycle scope
        slidingJob = viewLifecycleOwner.lifecycleScope.launch {
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

    override fun onDestroy() {
        super.onDestroy()

        slidingJob?.cancel()
        // viewPagertabs.adapter=null


    }


    override fun onDestroyView() {
        // اجعل الأداپتر فارغاً قبل تدمير الواجهة
        viewPagertabs.adapter = null
        super.onDestroyView()
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