package co.android.helper.hkaqi.ui.home.map.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import co.android.helper.hkaqi.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class MarkerInfoWindowAdapter(val context: Context, private val isFromMyTask: Boolean = false) : GoogleMap.InfoWindowAdapter {
    var view: View
    init {
        val getView = LayoutInflater.from(context).inflate(R.layout.layout_info_window,null)
        view = getView
    }

    override fun getInfoContents(p0: Marker): View =  view

    override fun getInfoWindow(p0: Marker): View = view
}