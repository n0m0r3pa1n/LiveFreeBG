package com.livefreebg.android.places

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.livefreebg.android.common.extensions.observeViewState
import com.livefreebg.android.databinding.FragmentPlacesBinding
import com.livefreebg.android.domain.places.Place
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow
import timber.log.Timber


@AndroidEntryPoint
class PlacesFragment : Fragment() {

    private val viewModel: PlacesViewModel by viewModels()

    private lateinit var getLocationPermission: ActivityResultLauncher<String>

    private val placeClickListener = CustomInfoWindow.OnPlaceClickListener {
        findNavController().navigate(PlacesFragmentDirections.actionPlacesToDetails(it))
    }

    private var _binding: FragmentPlacesBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance()
            .load(context, PreferenceManager.getDefaultSharedPreferences(context))

        getLocationPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    viewModel.getCoordinates()
                }
            }
        requestCoarsePermission()
    }

    private fun requestCoarsePermission() = activity?.let {
        try {
            val isPermissionGranted = ContextCompat.checkSelfPermission(
                it.applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (isPermissionGranted) {
                viewModel.getCoordinates()
            } else {
                getLocationPermission.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        } catch (e: Exception) {
            Timber.e(e, "Error requesting permission!")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlacesBinding.inflate(inflater, container, false).apply {
            setupViews()
            observeViewState(viewModel.uiState) {
                with(binding.map) {
                    it.center?.let {
                        controller.setCenter(it)
                    }
                    controller.setZoom(it.zoomLevel)

                    displayMarkers(it.places)
                }
            }
        }


        return binding.root
    }

    private fun MapView.displayMarkers(places: List<Place>) {
        overlays.clear()
        places.forEach {
            val startPoint = GeoPoint(it.lat, it.lng)
            val marker = Marker(this)
            marker.setInfoWindow(
                CustomInfoWindow(
                    it,
                    placeClickListener,
                    this
                )
            )
            marker.position = startPoint
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            overlays.add(marker)
        }
    }


    private fun FragmentPlacesBinding.setupViews() {
        with(map) {
            val mapEventsOverlay = MapEventsOverlay(context, object : MapEventsReceiver {
                override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                    InfoWindow.closeAllInfoWindowsOn(map);
                    return true
                }

                override fun longPressHelper(p: GeoPoint?): Boolean {
                    return false
                }

            })
            overlays.add(0, mapEventsOverlay);
            setUseDataConnection(true)
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            setBuiltInZoomControls(true)
            controller.setZoom(8.30)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddPlace.setOnClickListener {
            findNavController().navigate(PlacesFragmentDirections.actionPlacesToAdd())
        }
    }

    override fun onDestroyView() {
        viewModel.saveMapState(
            binding.map.mapCenter as GeoPoint,
            binding.map.zoomLevelDouble
        )

        super.onDestroyView()
        _binding = null
    }
}
