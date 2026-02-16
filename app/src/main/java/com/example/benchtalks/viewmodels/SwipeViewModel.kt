package com.example.benchtalks.viewmodels

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.benchtalks.domain.repository.SwipeRepository
import com.example.benchtalks.models.PersonCard
import com.example.benchtalks.models.UserLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SwipeViewModel(private val swipeRepository: SwipeRepository) : ViewModel() {
    private val _cards = MutableStateFlow<List<PersonCard>>(emptyList())
    val cards: StateFlow<List<PersonCard>> = _cards

    private val _responseEvent = MutableStateFlow(false)
    val responseEvent: StateFlow<Boolean> = _responseEvent

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _matchEvent = MutableStateFlow<Int?>(null)
    val matchEvent: StateFlow<Int?> = _matchEvent

    private val _locationUpdateSuccess = MutableStateFlow<Boolean?>(null)
    val locationUpdateSuccess: StateFlow<Boolean?> = _locationUpdateSuccess

    private val _location = MutableStateFlow<UserLocation?>(null)
    val location: StateFlow<UserLocation?> = _location

    fun updateLocation(location: Location) {
        _location.value = UserLocation(location.latitude, location.longitude)
    }

    fun getPersonsCards(userId: Int) {
        viewModelScope.launch {
            val result = swipeRepository.getSwipeFeed(userId)
            result.onSuccess { result ->
                _cards.value = result.map { profile ->
                    PersonCard(
                        id = profile.id,
                        name = profile.name,
                        age = profile.age,
                        about = profile.bio,
                    )
                }
            }.onFailure {
                _responseEvent.value = true
            }
            _responseEvent.value = false
        }
    }

    fun swipeUser(userId: Int, targetUserId: Int, isLike: Boolean) {
        viewModelScope.launch {
            val response = swipeRepository.swipeUser(userId, targetUserId, isLike)
            response.onSuccess {
                if (it.isMatch) {
                    _matchEvent.value = it.matchId
                }
            }
        }
    }

    fun updateUserLocation(userId: Int, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val result = swipeRepository.updateUserLocation(userId, latitude, longitude)
            result.onSuccess {
                Log.d("BenchViewModel", "Location updated successfully")
                _locationUpdateSuccess.value = true
            }.onFailure {
                Log.e("BenchViewModel", "Failed to update location", it)
                _locationUpdateSuccess.value = false
                _responseEvent.value = true
            }
        }
    }

    fun clearMatchEvent() {
        _matchEvent.value = null
    }
}