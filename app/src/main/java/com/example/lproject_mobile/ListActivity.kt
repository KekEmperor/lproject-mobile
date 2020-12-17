package com.example.lproject_mobile

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class ListActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        supportActionBar?.title = "Мої події"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val sp: SharedPreferences = getSharedPreferences("idInfo", Context.MODE_PRIVATE)

        GlobalScope.launch(Dispatchers.IO) {
            val events = getAllEvents()

            for (i in 0 until events.length()) {
                val item = JSONObject(events[i].toString())

                if (getRegistration(item.getString("_id"), sp)) {
                    val ef = BlankFragment.newInstance(
                        item.getString("name"),
                        item.getString("startDate"),
                        item.getString("locationCountry") + ", " + item.getString("locationCity"),
                        item.getString("_id"),
                        events[i].toString()
                    )

                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                    supportFragmentManager.beginTransaction()
                        .add(R.id.fragments_holder, ef)
                        .commit()
                }
            }
        }
    }
}