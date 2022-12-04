package me.ariy.mydex.data

import android.os.StrictMode
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class RESTUtils {

    fun get(client: OkHttpClient, url: String): Response {
        println("[GET] $url")
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val request = Request.Builder().url(url).get().build()
        return client.newCall(request).execute()
    }
}