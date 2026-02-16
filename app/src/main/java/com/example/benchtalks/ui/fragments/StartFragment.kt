package com.example.benchtalks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.benchtalks.databinding.FragmentStartBinding
import com.example.benchtalks.viewmodels.PersonInfoViewModel
import com.example.benchtalks.viewmodels.StartViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class StartFragment : Fragment() {
    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!
    private val startViewModel by activityViewModel<StartViewModel>()
    private val personInfoViewModel by activityViewModel<PersonInfoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.retryButton.setOnClickListener {
            startViewModel.userEmail.value?.let { userId ->
                handleNavigation(userId)
            }
        }

        startViewModel.userEmail.observe(viewLifecycleOwner) { userId ->
            handleNavigation(userId)
        }
    }

    private fun handleNavigation(userId: Int?) {
       if (userId == null) {
           navigateToNameFragment()
           return
       }
        viewLifecycleOwner.lifecycleScope.launch {
            showLoading()

            val userExists = personInfoViewModel.checkUser(userId)

            hideLoading()

            if (userExists) {
                val action = StartFragmentDirections.actionStartFragmentToSwipeFragment(userId)
                findNavController().navigate(action)
            } else {
                navigateToNameFragment()
            }
        }
    }

    private fun navigateToNameFragment() {
        val action = StartFragmentDirections.actionStartFragmentToNameFragment()
        findNavController().navigate(action)
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.errorIcon.isVisible = false
        binding.errorTextView.isVisible = false
        binding.errorMessageTextView.isVisible = false
        binding.retryButton.isVisible = false
    }

    private fun hideLoading() {
        binding.progressBar.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}