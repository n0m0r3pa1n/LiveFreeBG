package com.livefreebg.android.startup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.livefreebg.android.R
import com.livefreebg.android.databinding.FragmentStartupBinding
import com.livefreebg.android.extensions.observeViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartupFragment : Fragment() {
    private val viewModel: StartupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentStartupBinding.inflate(inflater).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewState(viewModel.navigateToLogin) {
            findNavController().navigate(R.id.action_startup_to_login)
        }
        observeViewState(viewModel.navigateToPlaces) {
            findNavController().navigate(R.id.action_startup_to_places)
        }
    }
}