package com.example.lproject_mobile

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_catalog_event.addressTextView
import kotlinx.android.synthetic.main.activity_catalog_event.nameTextView
import kotlinx.android.synthetic.main.activity_catalog_event.startDateTextView
import kotlinx.android.synthetic.main.activity_list_event.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import android.widget.*

class ListEventActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_event)
        supportActionBar?.title = getString(R.string.see_my_event)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val sp = getSharedPreferences("idInfo", Context.MODE_PRIVATE)

        val event = JSONObject(intent.extras?.getString("eventJSON")!!)

        nameTextView.text = event.getString("name")

        val startDate = ZonedDateTime.parse(event.getString("startDate"))
            .format(DateTimeFormatter.ofPattern(getString(R.string.date_formatter) + ", HH:mm"))
        startDateTextView.text = startDate

        val address =
            event.getString("locationCountry") + ", " +
                    event.getString("locationCity") + ", " +
                    event.getString("locationAddress") + " (" + event.getString("locationPlace") + ")"
        addressTextView.text = address

        if (ZonedDateTime.now() > ZonedDateTime.parse(event.getString("startDate"))) {
            GlobalScope.launch(Dispatchers.IO) {
                val visits = getVisitsByVisitor(event.getString("_id"), sp)

                for (i in 0 until visits.length()) {
                    val item = JSONObject(visits[i].toString())


                    var ll = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    ll.setMargins(0, 10, 0, 0)

                    val location = JSONObject(item.getString("location"))

                    runOnUiThread {
                        val tv = TextView(applicationContext)
                        tv.textSize = 20f
                        tv.text =
                            location.getString("name") + ": " +
                                    ZonedDateTime.parse(item.getString("startTime"))
                                        .format(DateTimeFormatter.ofPattern(getString(R.string.date_formatter) + " HH:mm:ss")) + " - " +
                                    ZonedDateTime.parse(item.getString("finishTime"))
                                        .format(DateTimeFormatter.ofPattern(getString(R.string.date_formatter) + " HH:mm:ss"))
                        tv.layoutParams = ll
                        visitsHolder.addView(tv)
                    }
                }

                if (ZonedDateTime.now() < ZonedDateTime.parse(event.getString("finishDate"))) {
                    var locations = getLocationsOfEvent(event.getString("_id"))
                    val arrayLoc: MutableList<String> = mutableListOf()

                    for (i in 0 until locations.length()) {
                        arrayLoc += (JSONObject(locations[i].toString()).getString("name"))
                    }

                    runOnUiThread {
                        val spinner = Spinner(applicationContext)
                        spinner.adapter = ArrayAdapter(
                            applicationContext,
                            android.R.layout.simple_spinner_item,
                            arrayLoc
                        )
                        var ll = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        ll.setMargins(0, 20, 0, 0)
                        spinner.layoutParams = ll
                        spinner.isClickable = sp.getString("visitStatus", "") != "finish"

                        visitsHolder.addView(spinner)

                        val button = Button(applicationContext)
                        button.text = getString(R.string.set_presence)

                        button.setOnClickListener() {
                            if (sp.getString("visitStatus", "") == "start"
                                || sp.getString("visitStatus", "") == "") {
                                sp.edit().putString("visitStatus", "finish").apply()
                                sp.edit().putString(
                                    "locId",
                                    JSONObject(locations[spinner.selectedItemPosition].toString()).getString(
                                        "_id"
                                    )
                                ).apply()
                                sp.edit()
                                    .putString("locPos", spinner.selectedItemPosition.toString())
                                    .apply()
                                sp.edit().putString(
                                    "startTime",
                                    ZonedDateTime.now().toString().split('[')[0]
                                ).apply()
                            } else {
                                sp.edit().putString("visitStatus", "start").apply()
                                GlobalScope.launch {
                                    setVisitOnLocation(sp)
                                }
                            }
                        }

                        visitsHolder.addView(button)
                    }
                }
            }
        } else {
            layoutHeader.text = ""
        }
    }
}