package com.example.lproject_mobile

import android.content.SharedPreferences
import android.util.Log
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import org.json.JSONArray

suspend fun getAllEvents(): JSONArray {
    HttpClient().use {
        val data = it.get<HttpResponse>("http://10.0.2.2:30030/event")
        var events = JSONArray()

        if (data.status == HttpStatusCode.OK) {
            events = JSONArray(data.readText())
        }

        return events
    }
}

suspend fun getRegistration(eventId: String, sp: SharedPreferences): Boolean {
    HttpClient().use {
        val data = it.get<HttpResponse>("http://10.0.2.2:30030/event/" + eventId + "/visitor/" + sp.getString("visitorId", ""))

        return data.status == HttpStatusCode.OK
    }
}

suspend fun registerForEvent(eventId: String, sp: SharedPreferences): String {
    HttpClient().use {
        val data = it.post<HttpResponse>("http://10.0.2.2:30030/visitor/" + sp.getString("visitorId", "") + "/setEvent") {
            body = TextContent(
                "{\"eventId\": \"${eventId}\"}",
                ContentType.Application.Json
            )
            header("x-auth-token", sp.getString("token", ""))
        }

        return data.status.toString().split(' ')[0]
    }
}

suspend fun getVisitsByVisitor(eventId: String, sp: SharedPreferences): JSONArray {
    HttpClient().use {
        val data = it.get<HttpResponse>("http://10.0.2.2:30030/event/" + eventId + "/visitor/" + sp.getString("visitorId", "") + "/getVisits")
        var events = JSONArray()

        if (data.status == HttpStatusCode.OK) {
            events = JSONArray(data.readText())
        }

        return events
    }
}