package com.example.benchtalks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.benchtalks.domain.datastore.UserPreferencesRepository
import com.example.benchtalks.domain.repository.SwipeRepository
import com.example.benchtalks.models.Gender
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PersonInfoViewModel(private val swipeRepository: SwipeRepository, private val dataStoreRepository: UserPreferencesRepository) : ViewModel() {

    private var _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private var _aboutText = MutableStateFlow("")
    val aboutText: StateFlow<String> = _aboutText

    private var _gender = MutableStateFlow(Gender.OTHER)
    val gender: StateFlow<Gender> = _gender

    private var _age = MutableStateFlow<Int?>(null)
    val age: StateFlow<Int?> = _age

    private var _email = MutableStateFlow<String?>(null)
    val email: StateFlow<String?> = _email

    private var _checkUserEvent = MutableStateFlow<Boolean?>(null)
    val checkUserEvent: StateFlow<Boolean?> = _checkUserEvent

    fun saveCheckUserEvent(userEvent: Boolean) {
        _checkUserEvent.value = userEvent
    }

    fun saveEmail(email: String) {
        _email.value = email
    }

    fun saveName(name: String) {
        _name.value = name
    }

    fun saveAbout(aboutText: String) {
        _aboutText.value = aboutText
    }

    fun saveGender(gender: Gender) {
        _gender.value = gender
    }

    fun saveAge(age: Int) {
        _age.value = age
    }

    fun registerAndGetId(onSuccess: (Int) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = swipeRepository.registerUser(
                name = name.value,
                gender = gender.value.toString(),
                bio = aboutText.value,
                age = age.value,
                photos = emptyList(),
                email = email.value
            )
            result.onSuccess { user ->
                onSuccess(user.id)
            }.onFailure { exception ->
                onError(exception.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun saveUserIdToPrefs(userId: Int) {
        viewModelScope.launch {
            dataStoreRepository.saveUserId(userId)
        }
    }

    fun checkUser(userId: Int) : Job {
        return viewModelScope.launch {
            val result = swipeRepository.getUserProfile(userId)
            result.onSuccess {
                _checkUserEvent.value = true
            }
            _checkUserEvent.value = false
        }
    }
}