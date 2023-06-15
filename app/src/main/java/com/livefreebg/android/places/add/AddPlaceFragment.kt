package com.livefreebg.android.places.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.livefreebg.android.databinding.FragmentAddPlaceBinding
import timber.log.Timber

class AddPlaceFragment : Fragment() {
    private var _binding: FragmentAddPlaceBinding? = null
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickMedia = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Timber.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Timber.d("PhotoPicker", "No media selected")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddPlaceBinding.inflate(inflater, container, false).apply {
            setupViews()
        }
        return binding.root

    }

    private fun FragmentAddPlaceBinding.setupViews() {
        imagePicker.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
