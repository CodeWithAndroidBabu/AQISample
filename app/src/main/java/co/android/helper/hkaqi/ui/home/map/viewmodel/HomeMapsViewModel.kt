package co.android.helper.hkaqi.ui.home.map.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.android.helper.hkaqi.R
import co.android.helper.hkaqi.data.model.AQISearchModel
import co.android.helper.hkaqi.data.model.AQICityNamedModel
import co.android.helper.hkaqi.data.model.AQIDetailsModel
import co.android.helper.hkaqi.state.NetworkState
import co.android.helper.hkaqi.ui.home.map.adapter.AQIListAdapter
import co.android.helper.hkaqi.ui.home.repo.MainRepo
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.RequestBody

class HomeMapsViewModel(private val myRepo: MainRepo) : ViewModel() {

    lateinit var currentJob: Job

    var keyword: String = ""
    var isApiRequesting: Boolean = false
    var aqiDetailsModel = AQIDetailsModel()
    private val aqiByCityObserver: MutableLiveData<NetworkState<AQICityNamedModel>> =
        MutableLiveData()
    private val aqiBySearchObserver: MutableLiveData<NetworkState<AQISearchModel>> =
        MutableLiveData()
    private val adapterClickItemObserver: MutableLiveData<AQISearchModel.Data> =
        MutableLiveData()
    private val aqiByGeoObserver: MutableLiveData<NetworkState<AQICityNamedModel>> =
        MutableLiveData()

    val getAqiByCityObserver: LiveData<NetworkState<AQICityNamedModel>>
        get() = aqiByCityObserver
    val getAqiBySearchObserver: LiveData<NetworkState<AQISearchModel>>
        get() = aqiBySearchObserver
    val getAdapterClickItemObserver: LiveData<AQISearchModel.Data>
        get() = adapterClickItemObserver
    val getAqiByGeoObserver: LiveData<NetworkState<AQICityNamedModel>>
        get() = aqiByGeoObserver

    val aqiListAdapter = AQIListAdapter(mutableListOf()){
        Log.d("fahsjfhas", ": $it")
        adapterClickItemObserver.value = it
    }

    fun getAQIByCity(city: String) {
        aqiByCityObserver.value = NetworkState.Loading()
        currentJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = myRepo.getAQIByCity(city)
                if (result.isSuccessful) {
                    val aqiModel = Gson().fromJson<AQICityNamedModel>(
                        result.body()?.string(),
                        AQICityNamedModel::class.java
                    )
                    aqiByCityObserver.postValue(NetworkState.Success(aqiModel))
                } else {
                    aqiByCityObserver.postValue(NetworkState.Failure("Something went wrong"))
                }
            }
        }
    }

    fun getAQIByCity(latLng: LatLng) {
        aqiByGeoObserver.value = NetworkState.Loading()
        currentJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = myRepo.getAQIByGeo(latLng)
                if (result.isSuccessful) {
                    result.body()?.let {
                        aqiByGeoObserver.postValue(NetworkState.Success(it))
                    }
                } else {
                    aqiByGeoObserver.postValue(NetworkState.Failure("Something went wrong"))
                }
            }
        }
    }

    fun getAQIBySearch(keyboard: String) {
        isApiRequesting = true
        aqiBySearchObserver.value = NetworkState.Loading()
        currentJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = myRepo.getAQIBySearch(keyboard)
                if (result.isSuccessful) {
                    Log.d("jjjhhhhbjbj", "getAQIBySearch: ${result.body()}")
                    result.body()?.let {
                        aqiBySearchObserver.postValue(NetworkState.Success(it))
                    }

                } else {
                    aqiBySearchObserver.postValue(NetworkState.Failure("Something went wrong"))
                }
                isApiRequesting = false
            }
        }
    }

    fun cancelCurrentJob(isJobCancelled: (Boolean) -> Unit) {
        if (::currentJob.isInitialized) {
            currentJob.cancel()
            isJobCancelled.invoke(currentJob.isCancelled)
        }
    }

    fun getCityByAQI(city: String){
        Log.d("nkfnakfnaskfa", "getCityByAQI:Started")
        viewModelScope.launch {
            myRepo.getCityByAQI(city).flowOn(Dispatchers.IO).catch { e->
                Log.d("nkfnakfnaskfa", "getCityByAQI:Exception ${e.message}")
            }.map {
                //
            }.collect{

                Log.d("nkfnakfnaskfa", "getCityByAQI:Result ${it}")
            }
        }
    }

    fun getAQIName(aqi: Int): String {
        when (aqi) {
            in 0..50 -> {
                return "Good"
            }
            in 51..100 -> {
                return "Moderate"
            }
            in 101..150 -> {
                return "Unhealthy-Moderate"
            }
            in 151..200 -> {
                return "Unhealthy"
            }
            in 201..300 -> {
                return "Very-Unhealthy"
            }
            else -> {
                return "Hazardous"
            }
        }
    }
    fun getAQIIconAndBg(aqi: Int,getResourceBgBack: (Pair<Int,Int>)->Unit)  {
        when (aqi) {
            in 0..50 -> {
                getResourceBgBack.invoke(Pair(R.drawable.icon_aqi_good,R.drawable.bg_aqi_bubble_good))
            }
            in 51..100 -> {
                getResourceBgBack.invoke(Pair(R.drawable.icon_aqi_moderate,R.drawable.bg_aqi_bubble_moderate))
            }
            in 101..150 -> {
                getResourceBgBack.invoke(Pair(R.drawable.icon_aqi_moderate_unhealthy,R.drawable.bg_aqi_bubble_unhealthy_moderate))
            }
            in 151..200 -> {
                getResourceBgBack.invoke(Pair(R.drawable.icon_aqi_unhealthy,R.drawable.bg_aqi_bubble_unhealthy))
            }
            in 201..300 -> {
                getResourceBgBack.invoke(Pair(R.drawable.icon_aqi_very_unhealthy,R.drawable.bg_aqi_bubble_very_unhealthy))
            }
            else -> {
                getResourceBgBack.invoke(Pair(R.drawable.icon_aqi_hazardous,R.drawable.bg_aqi_bubble_haradous))
            }
        }
    }
}