package co.android.helper.hkaqi.network

import co.android.helper.hkaqi.BuildConfig
import co.android.helper.hkaqi.flow_demo.RouteApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {
    private var instance: Retrofit? = null

    private fun getInstance(): Retrofit {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        synchronized(this) {
            if (instance == null) {
                return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .build()
            }
        }

        return instance!!
    }

    fun getApiClient(): ApiRoute = getInstance().create(ApiRoute::class.java)
    fun getRouteApiClient(): RouteApi = getInstance().create(RouteApi::class.java)
}