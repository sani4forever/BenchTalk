package com.example.benchtalks.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.View.OnClickListener
import androidx.navigation.fragment.findNavController

import com.example.benchtalks.databinding.FragmentAboutPersonBinding
import com.example.benchtalks.viewmodels.PersonInfoViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class AboutPersonFragment : Fragment() {
    private var _binding: FragmentAboutPersonBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModel<PersonInfoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutPersonBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnSave.setOnClickListener(onClickListener)
        }
    }

    private val onClickListener = OnClickListener {
        val aboutText = binding.etExpandable.text.toString()
        viewModel.saveAbout(aboutText)
        findNavController().navigate(AboutPersonFragmentDirections.actionAboutPersonFragmentToEmailFragment())
    }
}