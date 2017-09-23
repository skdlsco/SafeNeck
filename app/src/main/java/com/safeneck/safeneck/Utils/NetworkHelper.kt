package com.safeneck.safeneck.Utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import java.net.NetworkInterface


/**
 * Created by eka on 2017. 9. 22..
 */
class NetworkHelper(private val context: Context) {

    companion object {
        private val url = "http://soylatte.kr"
        private val port = 8080

        var retrofit: Retrofit? = null

        val networkInstance: RetrofitInterface
            get() {
                if (retrofit == null) {
                    retrofit = Retrofit.Builder()
                            .baseUrl(url + ":" + port)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                }
                return retrofit!!.create<RetrofitInterface>(RetrofitInterface::class.java!!)
            }

        fun returnNetworkState(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected
        }
    }
}
