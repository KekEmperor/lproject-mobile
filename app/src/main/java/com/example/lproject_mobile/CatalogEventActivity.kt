package com.example.lproject_mobile

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_catalog_event.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CatalogEventActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog_event)
        supportActionBar?.title = "Перегляд події"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val event = JSONObject(intent.extras?.getString("eventJSON")!!)

        nameTextView.text = event.getString("name")

        val startDate = ZonedDateTime.parse(event.getString("startDate"))
            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"))
        startDateTextView.text = startDate

        val finishDate = ZonedDateTime.parse(event.getString("finishDate"))
            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"))
        finishDateTextView.text = finishDate

        val address =
            event.getString("locationCountry") + ", " +
                    event.getString("locationCity") + ", " +
                    event.getString("locationAddress") + " (" + event.getString("locationPlace") + ")"
        addressTextView.text = address

        if (event.getString("description") != "") {
            descTextView.text = event.getString("description")
        } else {
            textView14.text = ""
            descTextView.text = ""
        }

        val sp = getSharedPreferences("idInfo", Context.MODE_PRIVATE)
        GlobalScope.launch(Dispatchers.IO) {
            if (getRegistration(event.getString("_id"), sp)) {
                runOnUiThread {
                    registerButton.isVisible = false
                }
            }
        }

        registerButton.setOnClickListener() {
            var status: String
            GlobalScope.launch(Dispatchers.IO) {
                status = registerForEvent(event.getString("_id"), sp)
                if (status == "200") {
                    runOnUiThread {
                        val builder = AlertDialog.Builder(this@CatalogEventActivity)
                        builder.setTitle("Успіх")
                        builder.setMessage("Ви успішно зареєструвалися на захід!")
                        builder.setPositiveButton(
                            "OK"
                        ) { _, _ ->
                            finish()
                            startActivity(intent)
                        }
                        builder.show()
                    }
                } else {
                    runOnUiThread {
                        val builder = AlertDialog.Builder(this@CatalogEventActivity)
                        builder.setTitle("Виникла помилка")
                        builder.setMessage("Сталася невідома нам помилка. Будь ласка, спробуйте пізніше.")
                        builder.show()
                    }
                }
            }
        }
    }
}