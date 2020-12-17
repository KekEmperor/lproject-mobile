package com.example.lproject_mobile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.suspendCoroutine


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val sp: SharedPreferences = getSharedPreferences("idInfo", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (sp.getString("token", "") != "") {
            moveToMenu(this)
        }

        loginButton.setOnClickListener() {
            var status = ""
            GlobalScope.launch(Dispatchers.IO) {
                status = login(editPhone.text.toString(), editPassword.text.toString(), sp)

                if (status == "200") {
                    moveToMenu(applicationContext)
                } else {
                    runOnUiThread {
                        showAlert()
                    }
                }
            }
        }
    }

    private fun moveToMenu(context: Context) {
        val i = Intent(context, MenuActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i)
        finish()
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Виникла помилка")
        builder.setMessage("Ви ввели неправильний номер телефону або пароль!")
        builder.show()
    }
}
