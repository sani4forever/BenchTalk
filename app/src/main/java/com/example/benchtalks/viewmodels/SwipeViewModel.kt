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

    private val _responseEvent = MutableStateFlow(false)
    val responseEvent: StateFlow<Boolean> = _responseEvent

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _matchEvent = MutableStateFlow<Int?>(null)
    val matchEvent: StateFlow<Int?> = _matchEvent

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
                _matchEvent.value = null
            }
        }
    }
}