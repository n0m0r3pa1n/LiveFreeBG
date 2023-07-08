package com.livefreebg.android.places.details

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.livefreebg.android.places.PlacesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceDetailsFragment : Fragment() {

    private val viewModel: PlacesViewModel by viewModels()


}