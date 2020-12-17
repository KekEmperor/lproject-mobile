package com.example.lproject_mobile

import android.os.Build
import android.os.Bundle
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.ZonedDateTime

class CatalogActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)
        supportActionBar?.title = "Каталог подій"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        GlobalScope.launch(Dispatchers.IO) {
            val events = getAllEvents()

            for (i in 0 until events.length()) {
                val item = JSONObject(events[i].toString())

                var startDate = ZonedDateTime.parse(item.getString("startDate"))

                if (startDate > ZonedDateTime.now()) {
                    val ef = BlankFragment.newInstance(
                        item.getString("name"),
                        item.getString("startDate"),
                        item.getString("locationCountry") + ", " + item.getString("locationCity"),
                        item.getString("_id"),
                        events[i].toString(),
                        "Unregistered"
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