package com.example.lproject_mobile

import android.content.SharedPreferences
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import org.json.*

suspend fun login(phone: String, password: String, sp: SharedPreferences): String {
    HttpClient().use {
        val data = it.post<HttpResponse>("http://10.0.2.2:30030/visitor/login") {
            body = TextContent(
                "{\"phoneNumber\": \"$phone\", \"password\": \"$password\"}",
                ContentType.Application.Json
            )
        }

        if (data.status == HttpStatusCode.OK) {
            val credentials = JSONObject(data.readText())

            sp.edit().putString("token", credentials.getString("token")).apply()
            sp.edit().putString("visitorId", credentials.getString("visitorId")).apply()

            val infoResponse = it.get<HttpResponse>("http://10.0.2.2:30030/visitor/" + sp.getString("visitorId", "")) {
                header("x-auth-token", sp.getString("token", ""))
            }
            val info = JSONObject(infoResponse.readText())

            sp.edit().putString("firstName", info.getString("firstName")).apply()
            sp.edit().putString("lastName", info.getString("lastName")).apply()
        }

        return data.status.toString().split(' ')[0]
    }
}

