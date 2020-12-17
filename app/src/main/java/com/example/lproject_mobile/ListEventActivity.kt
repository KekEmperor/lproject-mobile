package com.example.lproject_mobile

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginTop
import kotlinx.android.synthetic.main.activity_catalog_event.*
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

class ListEventActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_event)
        supportActionBar?.title = "Перегляд моєї події"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val sp = getSharedPreferences("idInfo", Context.MODE_PRIVATE)

        val event = JSONObject(intent.extras?.getString("eventJSON")!!)

        nameTextView.text = event.getString("name")

        val startDate = ZonedDateTime.parse(event.getString("startDate"))
            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"))
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
                        tv.text = location.getString("name") + ": " + ZonedDateTime.parse(item.getString("startTime"))
                            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm")) + " - " + ZonedDateTime.parse(item.getString("finishTime"))
                            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"))
                        tv.layoutParams = ll
                        visitsHolder.addView(tv)
                    }

                    //if (ZonedDateTime.now() < ZonedDateTime.parse(event.getString("finishDate"))) {
                        runOnUiThread {
                            val button = Button(applicationContext)
                            button.text = "Позначити присутність на локації"

                        }
                    //}
                }
            }
        } else {
            layoutHeader.text = ""
        }
    }
}