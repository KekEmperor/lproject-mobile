package com.example.lproject_mobile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_blank.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"
private const val ARG_PARAM4 = "param4"
private const val ARG_PARAM5 = "param5"
private const val ARG_PARAM6 = "param6"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var param3: String? = null
    private var param4: String? = null
    private var param5: String? = null
    private var param6: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            param3 = it.getString(ARG_PARAM3)
            param4 = it.getString(ARG_PARAM4)
            param5 = it.getString(ARG_PARAM5)
            param6 = it.getString(ARG_PARAM6)
        }
    }

    private lateinit var root: View

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_blank, container, false)

        val date = LocalDate.parse(param2?.split('T')?.get(0))
        val dateString = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        root.dateTextView.text = dateString

        val name: String = param1 ?: ""
        root.nameTextView.text = name

        val city: String = param3 ?: ""
        root.cityTextView.text = city

        root.setOnClickListener {
            var i: Intent = if (param6 == "Registered") {
                Intent(activity?.applicationContext, CatalogEventActivity::class.java)
            } else {
                Intent(activity?.applicationContext, ListEventActivity::class.java)
            }
            i.putExtra("eventJSON", param5)
            startActivity(i)
        }

        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Name.
         * @param param2 Date.
         * @param param3 City.
         * @param param4 _id.
         * @param param5 Event JSON string.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String, param3: String, param4: String, param5: String) =
            BlankFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, param3)
                    putString(ARG_PARAM4, param4)
                    putString(ARG_PARAM5, param5)
                }
            }
    }
}