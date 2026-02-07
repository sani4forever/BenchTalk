package com.example.benchtalks.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.benchtalks.domain.repository.SwipeRepository
import com.example.benchtalks.models.Gender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PersonInfoViewModel(private val swipeRepository: SwipeRepository) : ViewModel() {

    private var _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private var _aboutText = MutableStateFlow("")
    val aboutText: StateFlow<String> = _aboutText

    private var _gender = MutableStateFlow(Gender.OTHER)
    val gender: StateFlow<Gender> = _gender

    private var _age = MutableStateFlow<Int?>(null)
    val age: StateFlow<Int?> = _age


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
            try {
                val response = swipeRepository.registerUser(
                    name = name.value,
                    gender = gender.value.toString(),
                    bio = aboutText.value,
                    birthday = null,
                    photos = emptyList()
                )
                if (response != null) {
                    onSuccess(response.id)
                    Log.e("testing", "${response.id}, ${response.bio},${response.name}, ${response.gender}")
                } else {
                    onError("Ошибка: Сервер не ответил")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Ошибка: Нет соединения с сервером")
            }
        }
    }
}