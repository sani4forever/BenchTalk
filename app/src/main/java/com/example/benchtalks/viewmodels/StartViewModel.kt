package com.example.benchtalks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.benchtalks.domain.datastore.UserPreferencesRepository

class StartViewModel(repository: UserPreferencesRepository): ViewModel() {
    val userEmail = repository.userIdFlow.asLiveData()
}