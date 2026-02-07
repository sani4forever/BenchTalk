package com.example.benchtalks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.view.View.OnClickListener
import androidx.navigation.fragment.findNavController
import com.example.benchtalks.databinding.FragmentNameBinding
import com.example.benchtalks.viewmodels.PersonInfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NameFragment : Fragment() {

    private var _binding: FragmentNameBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PersonInfoViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            continueButton.setOnClickListener(onClickListener)
        }
    }

    private val onClickListener = OnClickListener {
        val name = binding.nameEditText.text.toString()
        viewModel.saveName(name)
        navigateToGenderFragment()
    }

    private fun navigateToGenderFragment() {
        findNavController().navigate(NameFragmentDirections.actionNameFragmentToGenderFragment())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}