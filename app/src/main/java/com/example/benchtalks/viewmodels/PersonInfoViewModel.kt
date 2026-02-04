package com.example.benchtalks.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PersonInfoViewModel: ViewModel() {
    private var _name = MutableStateFlow<String?>(null)
    val name: StateFlow<String?> = _name

    private var _aboutText = MutableStateFlow<String?>(null)
    val aboutText: StateFlow<String?> = _aboutText

    fun saveName(name: String) {
        _name.value = name
    }

    fun saveAbout(aboutText: String) {
        _aboutText.value = aboutText
    }
}