package com.example.benchtalks.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.benchtalks.databinding.FragmentBenchBinding
import com.example.benchtalks.models.Bench
import com.example.benchtalks.viewmodels.BenchViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker


class BenchFragment : Fragment() {
    private var _binding: FragmentBenchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModel<BenchViewModel>()
    private val args: BenchFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Configuration.getInstance().userAgentValue = context?.packageName
        _binding = FragmentBenchBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMap()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.benches.collect { benches ->
                    displayBenchesOnMap(benches)
                    Log.d("DEBUG", "Benches received: ${benches.size}")
                }
            }
        }
        viewModel.getBenches(args.matchId, args.userId)
    }

    private fun setupMap() {
        binding.mapView.apply {
            setMultiTouchControls(true)
            controller.setZoom(15.0)
            val startPoint = GeoPoint(53.887125, 27.589329)
            controller.setCenter(startPoint)
        }
    }

    private fun displayBenchesOnMap(benches: List<Bench>) {
        binding.mapView.overlays.clear()

        benches.forEach { bench ->
            val marker = Marker(binding.mapView)
            marker.position = GeoPoint(bench.latitude, bench.longitude)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            marker.title = "Скамейка #${bench.id}"
            marker.snippet = "Оценка: ${bench.score}\nДистанция: ${bench.totalDistance} км"

            marker.setOnMarkerClickListener { m, _ ->
                m.showInfoWindow()
                true
            }

            binding.mapView.overlays.add(marker)
        }

        if (benches.isNotEmpty()) {
            val firstBench = GeoPoint(benches[0].latitude, benches[0].longitude)
            binding.mapView.controller.animateTo(firstBench)
        }

        binding.mapView.invalidate()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}