package com.livefreebg.android.places

import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import org.osmdroid.bonuspack.R
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow


class CustomInfoWindow(mapView: MapView?) : MarkerInfoWindow(R.layout.bonuspack_bubble, mapView) {
    init {
        var btn = mView.findViewById<View>(R.id.bubble_moreinfo) as Button
        btn.isVisible = true
        btn.setOnClickListener { view ->
            Toast.makeText(
                view.context,
                "Button clicked",
                Toast.LENGTH_LONG
            ).show()
        }

    }
}