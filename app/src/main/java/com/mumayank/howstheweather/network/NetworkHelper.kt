package com.mumayank.howstheweather.network

import android.content.Context
import android.widget.Toast
import com.mumayank.howstheweather.BuildConfig
import com.mumayank.howstheweather.R
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkHelper {

    companion object {
        private const val CONNECT_TIMEOUT = 10
        private const val READ_TIMEOUT = 10

        fun makeApiCall(
            context: Context,
            urlString: String,
            callback: NetworkCallback?,
        ) {
            if (NetworkCheckHelper.isNetworkAvailable(context).not()) {
                Toast.makeText(
                    context,
                    context.getString(R.string.no_network),
                    Toast.LENGTH_SHORT
                ).show()
                callback?.onFailure()
                return
            }

            val networkService = Retrofit.Builder().baseUrl("https://api.openweathermap.org")
                .client(
                    OkHttpClient.Builder()
                        .readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
                        .connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                        .addInterceptor {
                            val original = it.request()
                            val originalHttpUrl = original.url
                            val url = originalHttpUrl.newBuilder()
                                .addQueryParameter("appid", BuildConfig.open_weather_map_api_key)
                                .build()
                            val requestBuilder = original.newBuilder().url(url)
                            val request = requestBuilder.build()
                            it.proceed(request)
                        }
                        .build()
                )
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(NetworkService::class.java)

            val call = networkService.apiGet(urlString)

            call.clone().enqueue(object : retrofit2.Callback<ResponseBody?> {

                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    when (response.code()) {
                        200 -> {
                            val body = response.body()
                            callback?.onSuccess(body)
                        }
                        else -> {
                            Toast.makeText(
                                context,
                                context.getString(R.string.no_network),
                                Toast.LENGTH_SHORT
                            ).show()
                            callback?.onFailure()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.no_network),
                        Toast.LENGTH_SHORT
                    ).show()
                    callback?.onFailure()
                }
            })

        }
    }

}