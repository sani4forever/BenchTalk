package com.example.benchtalks.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.benchtalks.databinding.FragmentEmailBinding
import com.example.benchtalks.viewmodels.PersonInfoViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class EmailFragment : Fragment() {

    private var _binding: FragmentEmailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModel<PersonInfoViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnContinueEmail.setOnClickListener {
            val email = binding.etEmail.text.toString()
            viewModel.saveEmail(email)
            findNavController().navigate(EmailFragmentDirections.actionEmailFragmentToAgeFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}