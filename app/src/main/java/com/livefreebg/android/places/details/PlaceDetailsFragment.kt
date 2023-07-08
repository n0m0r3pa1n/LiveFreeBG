package com.livefreebg.android.places.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.livefreebg.android.common.extensions.observeViewState
import com.livefreebg.android.databinding.FragmentPlaceDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaceDetailsFragment : Fragment() {

    @Inject
    lateinit var adapter: FirestorePicturesAdapter

    private val viewModel: PlaceDetailsViewModel by viewModels()

    private var _binding: FragmentPlaceDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setPlace(navArgs<PlaceDetailsFragmentArgs>().value.place)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentPlaceDetailsBinding.inflate(inflater, container, false).apply {
            setupViews()
            observeViewModel()
            _binding = this
        }.root
    }

    private fun FragmentPlaceDetailsBinding.observeViewModel() {
        observeViewState(viewModel.uiState) {
            it.place?.let {
                adapter.setPictures(it.pictures)
            }
        }
    }

    private fun FragmentPlaceDetailsBinding.setupViews() {
        gallery.adapter = adapter
    }
}