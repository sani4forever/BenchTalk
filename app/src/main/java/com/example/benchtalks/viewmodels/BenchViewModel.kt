package com.example.benchtalks.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.benchtalks.domain.repository.BenchRepository
import com.example.benchtalks.models.Bench
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BenchViewModel(private val benchRepository: BenchRepository) : ViewModel() {
    private val _benches = MutableStateFlow<List<Bench>>(emptyList())
    val benches = _benches.asStateFlow()

    private val _responseEvent = MutableStateFlow(false)
    val responseEvent: StateFlow<Boolean> = _responseEvent

    fun getBenches(matchId: Int, userId: Int) {
        viewModelScope.launch {
            val result = benchRepository.getBenches(matchId, userId)
            result.onSuccess { result ->
                _benches.value = result.map { bench ->
                    Bench(
                        bench.osmId,
                        bench.osmType,
                        bench.lat,
                        bench.lon,
                        bench.distanceUserAKm,
                        bench.distanceUserBKm,
                        bench.totalDistanceKm,
                        bench.score
                    )
                }
                Log.d("BenchViewModel", _benches.value.toString())
            }.onFailure {
                _responseEvent.value = true
            }
        }
    }

    fun getBenchesWithCoordinates(
        matchId: Int, lat1: Double, lon1: Double, lat2: Double, lon2: Double
    ) {
        viewModelScope.launch {
            val result = benchRepository.getBenchesWithCoordinates(matchId, lat1, lon1, lat2, lon2)
            result.onSuccess { result ->
                _benches.value = result.map { bench ->
                    Bench(
                        bench.osmId,
                        bench.osmType,
                        bench.lat,
                        bench.lon,
                        bench.distanceUserAKm,
                        bench.distanceUserBKm,
                        bench.totalDistanceKm,
                        bench.score
                    )
                }
            }.onFailure { _responseEvent.value = true }
        }
    }
}