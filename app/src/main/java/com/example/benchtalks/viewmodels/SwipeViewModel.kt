package com.example.benchtalks.viewmodels

import android.location.Location
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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _matchEvent = MutableStateFlow(false)
    val matchEvent: StateFlow<Boolean> = _matchEvent

    private val _location = MutableStateFlow<UserLocation?>(null)
    val location: StateFlow<UserLocation?> = _location

    fun updateLocation(location: Location) {
        _location.value = UserLocation(location.latitude, location.longitude)
    }

    fun getPersonsCards(userId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = swipeRepository.getSwipeFeed(userId)
                if (response != null) {
                    _cards.value = response.map { profile ->
                        PersonCard(
                            id = profile.id,
                            name = profile.name,
                            age = profile.age,
                            about = profile.bio,
                        )
                    }
                }
            }

            finally {
                _isLoading.value = false
            }
        }
    }

    fun swipeUser(userId: Int, targetUserId: Int, isLike: Boolean) {
        viewModelScope.launch {
            try {
                val response = swipeRepository.swipeUser(userId, targetUserId, isLike)
                if (response != null && response.isMatch) {
                    _matchEvent.value = true
                }
            } finally {

            }
        }
    }
}