package co.android.helper.hkaqi.ui.home

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import co.android.helper.hkaqi.BaseActivity
import co.android.helper.hkaqi.R
import co.android.helper.hkaqi.databinding.ActivityMainBinding
import co.android.helper.hkaqi.factory.ViewModelFactory
import co.android.helper.hkaqi.network.ApiClient
import co.android.helper.hkaqi.state.NetworkState
import co.android.helper.hkaqi.ui.home.repo.MainRepo
import co.android.helper.hkaqi.ui.home.map.viewmodel.HomeMapsViewModel

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private lateinit var viewModel: HomeMapsViewModel
    private val viewModelFactory = ViewModelFactory<MainRepo>(MainRepo(ApiClient.getApiClient(),ApiClient.getRouteApiClient()))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeMapsViewModel::class.java]
        observers()
        viewModel.getAQIByCity("hongkong")
    }

    private fun observers() {
        viewModel.getAqiByCityObserver.observe(this) {
            when (it) {
                is NetworkState.Loading -> {

                    Log.d("fanfknak", "observers: loading")
                }
                is NetworkState.Success<*> -> {
                    Log.d("fanfknak", "observers: success ${it.data}")
                }
                is NetworkState.Failure -> {
                    Log.d("fanfknak", "observers: failure")
                }
            }
        }

        viewModel.getAqiBySearchObserver.observe(this) {
            when (it) {
                is NetworkState.Loading -> {

                    Log.d("fanfknak", "observers: loading")
                }
                is NetworkState.Success<*> -> {
                    Log.d("fanfknak", "observers: success ${it.data}")
                }
                is NetworkState.Failure -> {
                    Log.d("fanfknak", "observers: failure")
                }
            }
        }
    }
}