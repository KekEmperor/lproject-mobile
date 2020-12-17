package com.example.lproject_mobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val sp = getSharedPreferences("idInfo", Context.MODE_PRIVATE)
        greeting.text = "Вітаємо, " + sp.getString("firstName", "") + " " + sp.getString("lastName", "")

        allEventsButton.setOnClickListener() {
            val i = Intent(this, CatalogActivity::class.java)
            startActivity(i)
        }
    }
}