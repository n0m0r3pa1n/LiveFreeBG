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
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import timber.log.Timber

@AndroidEntryPoint
class PlacesFragment : Fragment() {

    private val viewModel: PlacesViewModel by viewModels()

    private lateinit var getLocationPermission: ActivityResultLauncher<String>

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
                }
            }
        }


        return binding.root
    }

    private fun FragmentPlacesBinding.setupViews() {
        with(map) {
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
