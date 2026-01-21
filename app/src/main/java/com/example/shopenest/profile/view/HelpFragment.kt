package com.example.shopenest.profile.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.DisplayPhoto
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.shopenest.R

class HelpFragment : Fragment() {


    lateinit var whatsApp: LinearLayout
    lateinit var phone: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        whatsApp = view.findViewById(R.id.layoutWhatsapp)
        phone = view.findViewById(R.id.layoutPhone)

        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarSupport)
        phone.setOnClickListener {

            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:01012345678")
            startActivity(intent)
        }

        whatsApp.setOnClickListener {

            val intent = Intent(Intent.ACTION_VIEW)
// استبدل الرقم برقمك الحقيقي بالصيغة الدولية
            val url = "https://wa.me/20123456789?text=أهلاً، أحتاج مساعدة في طلبي"
            intent.data = Uri.parse(url)

            startActivity(intent)
        }

        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HelpFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HelpFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}