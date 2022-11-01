package co.android.helper.hkaqi.ui.home.repo

import co.android.helper.hkaqi.flow_demo.RouteApi
import co.android.helper.hkaqi.network.ApiRoute
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonElement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody

class MainRepo(private val apiRoute: ApiRoute,private val routeApi: RouteApi) {
    suspend fun getAQIByCity(city: String) =  apiRoute.getAQIByCity(city = city)
    suspend fun getAQIBySearch(keyword: String) =  apiRoute.getAQIBySearch(keyword = keyword)
    suspend fun getAQIByGeo(latLng: LatLng) =  apiRoute.getAQIByGeo(lat = latLng.latitude, lng = latLng.longitude)

    suspend fun getCityByAQI(city: String):Flow<JsonElement> = flow {
        emit(routeApi.getCityByAQI(city).body()!!)
    }
}