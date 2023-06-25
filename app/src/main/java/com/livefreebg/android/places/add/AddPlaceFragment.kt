package com.livefreebg.android.places.add

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.livefreebg.android.databinding.FragmentAddPlaceBinding
import com.livefreebg.android.extensions.observeViewState
import com.livefreebg.android.places.add.gallery.PicturesAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AddPlaceFragment : Fragment() {

    private val viewModel: AddPlacesViewModel by viewModels()

    private var _binding: FragmentAddPlaceBinding? = null
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var getLocationPermission: ActivityResultLauncher<String>

    private val adapter = PicturesAdapter()

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickMedia = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uris.isNotEmpty()) {
                adapter.setPictures(uris)
            } else {
                Timber.d("PhotoPicker", "No media selected")
            }
        }

        getLocationPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {isGranted ->
            if (isGranted) {
                viewModel.getCoordinates()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentAddPlaceBinding.inflate(inflater, container, false).apply {
            setupViews()
            observeViewModel()
        }
        return binding.root

    }

    private fun FragmentAddPlaceBinding.observeViewModel() {
        observeViewState(viewModel.uiState) {
            it.coordinates?.let {
                latitudeEditText.setText(it.first)
                longtitudeEditText.setText(it.second)
            }
        }
    }

    private fun FragmentAddPlaceBinding.setupViews() {
        imagePicker.setOnClickListener { requestImagePickPermission() }
        myLocation.setOnClickListener { requestCoarsePermission() }
        gallery.adapter = adapter
    }

    private fun requestImagePickPermission() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
