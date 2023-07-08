package com.livefreebg.android.places

import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import com.livefreebg.android.R
import com.livefreebg.android.domain.places.Place
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow
import org.osmdroid.bonuspack.R as RBonus


class CustomInfoWindow(
    private val place: Place,
    onPlaceClickListener: OnPlaceClickListener,
    mapView: MapView?,
) : MarkerInfoWindow(RBonus.layout.bonuspack_bubble, mapView) {
    init {
        val btn = mView.findViewById<Button>(RBonus.id.bubble_moreinfo)
        btn.isVisible = true
        btn.text = mapView?.context?.getString(R.string.details)

        btn.setOnClickListener { view ->
            onPlaceClickListener.onPlaceClick(place)
        }
    }

    override fun onOpen(item: Any?) {
        super.onOpen(item)
        val description = mView.findViewById<TextView>(RBonus.id.bubble_description)
        description.text = place.description
    }

    fun interface OnPlaceClickListener {
        fun onPlaceClick(place: Place)
    }
}