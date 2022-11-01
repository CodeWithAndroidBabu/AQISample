package co.android.helper.hkaqi.ui.home.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import co.android.helper.hkaqi.BaseActivity
import co.android.helper.hkaqi.R
import co.android.helper.hkaqi.bottom.AQIDetailsBottomDialog
import co.android.helper.hkaqi.common.DialogMsg
import co.android.helper.hkaqi.data.AppConstants
import co.android.helper.hkaqi.data.model.AQICityNamedModel
import co.android.helper.hkaqi.data.model.AQIDetailsModel
import co.android.helper.hkaqi.data.model.AQISearchModel
import co.android.helper.hkaqi.data.model.MAURITIUS
import co.android.helper.hkaqi.databinding.ActivityHomeMapsBinding
import co.android.helper.hkaqi.databinding.LayoutCustomMarkerBinding
import co.android.helper.hkaqi.factory.ViewModelFactory
import co.android.helper.hkaqi.network.ApiClient
import co.android.helper.hkaqi.state.NetworkState
import co.android.helper.hkaqi.ui.home.map.adapter.MarkerInfoWindowAdapter
import co.android.helper.hkaqi.ui.home.map.viewmodel.HomeMapsViewModel
import co.android.helper.hkaqi.ui.home.repo.MainRepo
import co.android.helper.hkaqi.utils.makeInvisible
import co.android.helper.hkaqi.utils.makeVisible
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class HomeMapsActivity : BaseActivity<ActivityHomeMapsBinding>(R.layout.activity_home_maps),
    OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var viewModel: HomeMapsViewModel
    private lateinit var fusedCurrentLocationListener: FusedLocationProviderClient
    private val viewModelFactory = ViewModelFactory<MainRepo>(MainRepo(ApiClient.getApiClient(),ApiClient.getRouteApiClient()))
    private val handler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }
    private val aqiDetailsBottomDialog: AQIDetailsBottomDialog by lazy {
        AQIDetailsBottomDialog.newInstance()
    }
    private lateinit var runnable: Runnable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeStatusBarTransparent()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel = ViewModelProvider(this, viewModelFactory)[HomeMapsViewModel::class.java]
        observers()
        viewModel.getCityByAQI("hongkong")
        //checkAndRequestForLocationPermission()
        progressDialog.show()
        runnable = Runnable {
            viewModel.getAQIBySearch(viewModel.keyword)
        }

        binding.edSearch?.doOnTextChanged { text, start, before, count ->
            Log.d("fabjfbjafs", "onCreate: $text - ${text?.contains("mau", true)}")
            handler.removeCallbacks(runnable)
            if (text?.isNotEmpty() == true) {
                binding.flCancelRequest?.makeInvisible(false)
                if (text.contains("mau", true)) {
                    viewModel.aqiListAdapter.setDefaultItemsForMauritius()
                    viewModel.cancelCurrentJob { isJobCancelled ->
                        if (isJobCancelled) {
                            binding.pb?.makeInvisible(true)
                        }
                    }
                } else {
                    viewModel.keyword = text.toString()
                    handler.postDelayed(runnable, 500)
                }
            } else {
                binding.flCancelRequest?.makeInvisible(true)
            }
        }

        binding.flCancelRequest?.setOnClickListener {
            if (!viewModel.isApiRequesting) {
                binding.edSearch?.setText("")
                binding.tvNoItemFound?.makeVisible(true)
                viewModel.aqiListAdapter.clearList()
                return@setOnClickListener
            }
            viewModel.cancelCurrentJob { isJobCancelled ->
                if (isJobCancelled) {
                    binding.pb?.makeInvisible(true)
                }
            }
            binding.flCancelRequest?.makeInvisible(true)
        }
        binding.cv?.setOnClickListener {
            //  binding.recyclerAQICity?.makeVisible(false)

        }
        setupAqiAdapter()
        Handler(Looper.getMainLooper()).postDelayed({
            if (::mMap.isInitialized) {
                mauritiusDefaultLocation()
            } else {
                DialogMsg.showMsg(this, msg = "Something went wrong, Please try again") {
                    mauritiusDefaultLocation()
                }
            }
            progressDialog.dismiss()
        }, 1500)
    }

    private fun mauritiusDefaultLocation() {

        val latLng = LatLng(
            viewModel.aqiListAdapter.getDefaultMauritiusFirstItem()[0],
            viewModel.aqiListAdapter.getDefaultMauritiusFirstItem()[1]
        )
        viewModel.aqiDetailsModel.aqi = "${viewModel.aqiListAdapter.apiRandom}"
        viewModel.aqiDetailsModel.cityName = AppConstants.Value.MAURITIUS_PORT_LOUIS
        viewModel.aqiDetailsModel.defaultCityName = MAURITIUS.PORT_LOUIS.name
        setCustomMarkerAndUpdateCamera(
            latLng,
            "${viewModel.aqiListAdapter.apiRandom}",
            AppConstants.Value.MAURITIUS_PORT_LOUIS,
            MAURITIUS.PORT_LOUIS.name
        )
        Log.d("fahkfahk", "mauritiusDefaultLocation: ")
        showBottomDialog(viewModel.aqiDetailsModel)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMapToolbarEnabled = false

        val markerWindowView = MarkerInfoWindowAdapter(this)
        googleMap.setInfoWindowAdapter(markerWindowView)
        googleMap.setOnInfoWindowClickListener { marker ->
            Log.d("fabfja", "setOnInfoWindowClickListener: ${marker.tag}")
            val getTag = marker.tag as? AQIDetailsModel
            val details = AQIDetailsModel().apply {
                this.aqi = getTag?.aqi ?: ""
                this.cityName = getTag?.cityName ?: ""
                this.defaultCityName = getTag?.defaultCityName ?: MAURITIUS.NOTHING.name
            }
            marker.hideInfoWindow()
            showBottomDialog(details)
        }


        // Add a marker in Sydney and move the camera
        // val sydney = LatLng(-34.0, 151.0)
        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap.isMyLocationEnabled = false
        mMap.uiSettings.isMyLocationButtonEnabled = true

        /*  mMap.setOnMarkerClickListener {
              Log.d("fabfja", "onMapReady: ${it.tag}")
              val getTag = it.tag as? AQIDetailsModel
              if(getTag != null){
                  val details = AQIDetailsModel().apply {
                      this.aqi = getTag.aqi
                      this.cityName = getTag.cityName
                  }
                  showBottomDialog(details)
                  return@setOnMarkerClickListener  true
              }

              return@setOnMarkerClickListener false
          }
          mMap.setOnInfoWindowClickListener {
              Log.d("fabfja", "setOnInfoWindowClickListener: ${it.tag}")
          }*/


    }

    private fun observers() {
        viewModel.getAqiByCityObserver.observe(this) {
            when (it) {
                is NetworkState.Loading -> {
                    binding.pb?.makeInvisible(false)
                }
                is NetworkState.Success<*> -> {
                    binding.pb?.makeInvisible(true)

                    Log.d("fabjfbjafs", "city result: ${it.data}")
                }
                is NetworkState.Failure -> {
                    binding.pb?.makeInvisible(true)
                    DialogMsg.showMsg(this, msg = it.error) {}
                }
            }
        }

        viewModel.getAqiByGeoObserver.observe(this) {
            when (it) {
                is NetworkState.Loading -> {
                    progressDialog.show()
                }
                is NetworkState.Success<*> -> {
                    progressDialog.dismiss()
                    val getRequiredData = it.data as AQICityNamedModel
                    viewModel.aqiDetailsModel.aqi = "${getRequiredData.data.aqi}"
                    viewModel.aqiDetailsModel.cityName = getRequiredData.data.city.name
                    val latLng =
                        LatLng(getRequiredData.data.city.geo[0], getRequiredData.data.city.geo[1])
                    setCustomMarkerAndUpdateCamera(
                        latLng,
                        getRequiredData.data.aqi.toString(),
                        getRequiredData.data.city.name,
                        MAURITIUS.NOTHING.name
                    )
                    showBottomDialog(viewModel.aqiDetailsModel)
                    Log.d("fabjfbjafs", "city result: ${it.data}")
                }
                is NetworkState.Failure -> {
                    progressDialog.dismiss()
                    DialogMsg.showMsg(this, msg = it.error) {}
                }
            }
        }

        viewModel.getAqiBySearchObserver.observe(this) {
            when (it) {
                is NetworkState.Loading -> {
                    binding.pb?.makeInvisible(false)
                }
                is NetworkState.Success<*> -> {
                    binding.pb?.makeInvisible(true)
                    val getRequiredData = it.data as AQISearchModel
                    if (getRequiredData.data.isNotEmpty()) {
                        binding.tvNoItemFound?.makeVisible(true)
                    } else {
                        binding.tvNoItemFound?.makeVisible(false)
                    }

                    viewModel.aqiListAdapter.refreshList(getRequiredData.data)
                    Log.d("fabjfbjafs", "search result result: ${getRequiredData.data.size}")
                }
                is NetworkState.Failure -> {
                    binding.pb?.makeInvisible(true)
                    DialogMsg.showMsg(this, msg = it.error) {}
                }
            }
        }
        viewModel.getAdapterClickItemObserver.observe(this) {
            closeKeyboard()
            viewModel.aqiListAdapter.clearList()
            viewModel.aqiDetailsModel.aqi = it.aqi
            viewModel.aqiDetailsModel.cityName = it.station.name
            viewModel.aqiDetailsModel.defaultCityName = when(it.station.name){
                AppConstants.Value.MAURITIUS_PORT_LOUIS->MAURITIUS.PORT_LOUIS.name
                AppConstants.Value.MAURITIUS_QUATRE_BORNES-> MAURITIUS.QUATRE_BORNES.name
                else->MAURITIUS.NOTHING.name
            }
            Log.d("fabjasss", "observers: ${it.station.name}")
            setCustomMarkerAndUpdateCamera(
                LatLng(it.station.geo[0], it.station.geo[1]),
                it.aqi, it.station.name,
                defaultCityName = viewModel.aqiDetailsModel.defaultCityName
            )
            showBottomDialog(viewModel.aqiDetailsModel)
        }
    }

    private fun checkAndRequestForLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher()
        } else {
            Log.d("fabjfasfa", "getLastLocation:getLastLocationgetLastLocation ")
            getLastLocation()
        }
    }

    private fun requestPermissionLauncher() {
        Log.d("fabjfasfa", "requestPermissionLauncher  ")
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Log.d("fabjfasfa", "requestPermissionLauncher isGranted ")
                getLastLocation()
            } else {
                DialogMsg.showMsg(
                    this, msg = "You have denied location permission but don't worry," +
                            "You can get the AQI of city details by searching"
                ) {}
            }
        }
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

    }

    private fun getLastLocation() {
        fusedCurrentLocationListener =
            LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedCurrentLocationListener.lastLocation.addOnSuccessListener { location ->
            Log.d("fabjfasfa", "getLastLocation:${location.latitude} -- ${location.longitude} ")
            if (location != null) {
                val latLng = LatLng(location.latitude, location.longitude)
                viewModel.getAQIByCity(latLng)
                /*ContextCompat.getDrawable(this, R.drawable.icon_aqi_good)?.let {
                    mMap.addMarker(
                        MarkerOptions().position(latLng)
                            .icon(BitmapDescriptorFactory.fromBitmap(getCustomerMarker(R.drawable.icon_aqi_good)))
                    )
                }*/
//                setCustomMarkerAndUpdateCamera(latLng,  "1")
                /* mMap.addMarker(
                     MarkerOptions().position(latLng)
                         .icon(BitmapDescriptorFactory.fromBitmap(getCustomerMarker(R.drawable.icon_aqi_good)))
                 )
                 val cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(
                     LatLng(
                         location.latitude,
                         location.longitude
                     ),15f
                 )
                 mMap.moveCamera(cameraUpdateFactory)
                 mMap.animateCamera(cameraUpdateFactory)*/
            }
        }.addOnFailureListener { e ->
            e.printStackTrace()
            DialogMsg.showMsg(this, msg = "Something went wrong while fetching your location") {}
        }
    }

    private fun getCustomerMarker(aqi: String): Bitmap {
        val customMarkerView = DataBindingUtil.inflate<LayoutCustomMarkerBinding>(
            LayoutInflater.from(this),
            R.layout.layout_custom_marker,
            null,
            false
        )
        try {
            viewModel.getAQIIconAndBg(aqi.toInt()) {
                customMarkerView.ivMarkerIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        it.first
                    )
                )
                customMarkerView.rlBg.background =
                    ContextCompat.getDrawable(this, it.second)
                customMarkerView.tvAQINo.text = aqi
            }
        } catch (e: Exception) {
            /**
             * If format exceptions error, must show UI as unavailable by using gray bg & place holder img
             * */
            customMarkerView.ivMarkerIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.img_place_holder
                )
            )
            customMarkerView.rlBg.background =
                ContextCompat.getDrawable(this, R.drawable.bg_aqi_bubble_grey)
            customMarkerView.tvAQINo.text = "---"
        }

        customMarkerView.root.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        customMarkerView.root.layout(
            0,
            0,
            customMarkerView.root.measuredWidth,
            customMarkerView.root.measuredHeight
        )

        customMarkerView.root.buildDrawingCache()
        val returnedBitmap = Bitmap.createBitmap(
            customMarkerView.root.measuredWidth,
            customMarkerView.root.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        val drawable = customMarkerView.root.background

        drawable?.draw(canvas)
        customMarkerView.root.draw(canvas)
        return returnedBitmap
    }

    private fun setupAqiAdapter() {
        binding.recyclerAQICity?.adapter = viewModel.aqiListAdapter
    }

    private fun setCustomMarkerAndUpdateCamera(
        latLng: LatLng,
        aqi: String,
        name: String,
        defaultCityName: String
    ) {
        val details = AQIDetailsModel().apply {
            this.aqi = aqi
            this.cityName = name
            this.defaultCityName = defaultCityName
        }
        mMap.addMarker(
            MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(getCustomerMarker(aqi)))
        )?.tag = details

        val cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(
            LatLng(
                latLng.latitude,
                latLng.longitude
            ), 10f
        )

        Handler(Looper.getMainLooper()).postDelayed({
            mMap.moveCamera(cameraUpdateFactory)
            mMap.animateCamera(cameraUpdateFactory, 1500, null)
        }, 500)
    }

    private fun showBottomDialog(data: AQIDetailsModel) {
        aqiDetailsBottomDialog.arguments = Bundle().apply {
            this.putSerializable(AppConstants.Keys.SERIALIZABLE_DATA, data)
        }
        Log.d("fabjfbjsafa", "showBottomDialog: $data")
        if (aqiDetailsBottomDialog.dialog?.isShowing == true) {
            aqiDetailsBottomDialog.dialog?.dismiss()

            aqiDetailsBottomDialog.show(
                supportFragmentManager,
                AQIDetailsBottomDialog::class.java.name
            )
        } else {
            aqiDetailsBottomDialog.show(
                supportFragmentManager,
                AQIDetailsBottomDialog::class.java.name
            )
        }
    }
}