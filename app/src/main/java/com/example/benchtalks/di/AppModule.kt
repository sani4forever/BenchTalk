package com.example.benchtalks.di

import com.example.benchtalks.viewmodels.PersonInfoViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { PersonInfoViewModel() }
}