package com.livefreebg.android.places

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.livefreebg.android.common.extensions.observeViewState
import com.livefreebg.android.databinding.FragmentPlacesBinding
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint

@AndroidEntryPoint
class PlacesFragment : Fragment() {

    private val viewModel: PlacesViewModel by viewModels()

    private var zoomLevel: Double? = null

    private var _binding: FragmentPlacesBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance()
            .load(context, PreferenceManager.getDefaultSharedPreferences(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlacesBinding.inflate(inflater, container, false).apply {
            setupViews()
            observeViewState(viewModel.uiState) {
                it.center?.let {
                    binding.map.controller.setCenter(it)
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
        zoomLevel?.let {
            binding.map.controller.setZoom(it)
        }

        binding.fabAddPlace.setOnClickListener {
            findNavController().navigate(PlacesFragmentDirections.actionPlacesToAdd())
        }
    }

    override fun onDestroyView() {
        viewModel.setCenter(binding.map.mapCenter as GeoPoint)
        zoomLevel = binding.map.zoomLevelDouble
        super.onDestroyView()
        _binding = null
    }
}
