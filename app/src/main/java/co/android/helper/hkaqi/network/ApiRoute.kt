package co.android.helper.hkaqi.network

import co.android.helper.hkaqi.BuildConfig
import co.android.helper.hkaqi.data.AppConstants
import co.android.helper.hkaqi.data.model.AQICityNamedModel
import co.android.helper.hkaqi.data.model.AQISearchModel
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiRoute {
    @GET(AppConstants.Network.AQI_BY_CITY)
    suspend fun getAQIByCity(
        @Path("city") city: String,
        @Query("token") token: String = BuildConfig.TOKEN
    ): Response<ResponseBody>

    @GET(AppConstants.Network.AQI_BY_SEARCH)
    suspend fun getAQIBySearch(
        @Query("keyword") keyword: String,
        @Query("token") token: String = BuildConfig.TOKEN
    ): Response<AQISearchModel>

    @GET(AppConstants.Network.AQI_BY_GEO)
    suspend fun getAQIByGeo(
        @Path("lat") lat: Double,
        @Path("lng") lng: Double,
        @Query("token") token: String = BuildConfig.TOKEN
    ): Response<AQICityNamedModel>
}