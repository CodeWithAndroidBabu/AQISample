package co.android.helper.hkaqi.flow_demo

import co.android.helper.hkaqi.BuildConfig
import co.android.helper.hkaqi.data.AppConstants
import com.google.gson.JsonElement
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RouteApi {
    @GET(AppConstants.Network.AQI_BY_CITY)
    suspend fun getCityByAQI(
        @Path("city") city: String,
        @Query("token") token: String = BuildConfig.TOKEN
    ): Response<JsonElement>
}