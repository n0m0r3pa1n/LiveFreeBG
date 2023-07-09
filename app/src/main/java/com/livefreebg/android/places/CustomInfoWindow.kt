package com.livefreebg.android.places

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.livefreebg.android.R
import com.livefreebg.android.domain.places.Place
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow

class CustomInfoWindow(
    private val place: Place,
    private val onPlaceClickListener: OnPlaceClickListener,
    mapView: MapView?,
) : MarkerInfoWindow(R.layout.marker_info_window, mapView) {
    init {

    }

    override fun onOpen(item: Any?) {
        super.onOpen(item)

        val description = mView.findViewById<TextView>(R.id.bubble_description)
        description.text = place.description

        val btn = mView.findViewById<View>(R.id.more_info) as Button
        btn.setOnClickListener { view ->
            onPlaceClickListener.onPlaceClick(place)
        }
    }

    fun interface OnPlaceClickListener {
        fun onPlaceClick(place: Place)
    }
}