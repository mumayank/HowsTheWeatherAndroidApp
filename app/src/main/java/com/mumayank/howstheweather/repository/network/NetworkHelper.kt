package com.mumayank.howstheweather.repository.network

import android.content.Context
import android.widget.Toast
import com.mumayank.howstheweather.BuildConfig
import com.mumayank.howstheweather.R
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.suspendCoroutine

class NetworkHelper {

    companion object {

        suspend fun makeApiCall(
            context: Context,
            urlString: String
        ): ResponseBody? {
            return suspendCoroutine { cont ->

                if (NetworkCheckHelper.isNetworkAvailable(context).not()) {
                    cont.resumeWith(Result.success(null))
                    return@suspendCoroutine
                }

                val networkService = Retrofit.Builder().baseUrl("https://api.openweathermap.org")
                    .client(
                        OkHttpClient.Builder()
                            .readTimeout(NetworkConstants.READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
                            .connectTimeout(NetworkConstants.CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                            .addInterceptor {
                                val original = it.request()
                                val originalHttpUrl = original.url
                                val url = originalHttpUrl.newBuilder()
                                    .addQueryParameter(
                                        "appid",
                                        BuildConfig.open_weather_map_api_key
                                    )
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

                    override fun onFailure(call: retrofit2.Call<ResponseBody?>, t: Throwable) {
                        cont.resumeWith(Result.success(null))
                    }

                    override fun onResponse(
                        call: retrofit2.Call<ResponseBody?>,
                        response: retrofit2.Response<ResponseBody?>
                    ) {
                        when (response.code()) {
                            200 -> {
                                cont.resumeWith(Result.success(response.body()))
                            }
                            else -> {
                                cont.resumeWith(Result.success(null))
                            }
                        }
                    }
                })

            }
        }
    }

}