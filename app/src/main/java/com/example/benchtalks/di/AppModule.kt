package com.example.benchtalks.di

import com.example.benchtalks.domain.api.BenchApi
import com.example.benchtalks.domain.api.SwipeApi
import com.example.benchtalks.domain.datastore.UserPreferencesRepository
import com.example.benchtalks.domain.repository.BenchRepository
import com.example.benchtalks.domain.repository.SwipeRepository
import com.example.benchtalks.viewmodels.BenchViewModel
import com.example.benchtalks.viewmodels.PersonInfoViewModel
import com.example.benchtalks.viewmodels.StartViewModel
import com.example.benchtalks.viewmodels.SwipeViewModel
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://heavenly-armoured-nyla.ngrok-free.dev/api/v1/"

val appModule = module {
    viewModel { PersonInfoViewModel(get(), get()) }
    viewModel { SwipeViewModel(get()) }
    viewModel { StartViewModel(get()) }
    viewModel { BenchViewModel(get()) }


    single {
        OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<SwipeApi> { get<Retrofit>().create(SwipeApi::class.java) }

    single { SwipeRepository(get()) }

    single { UserPreferencesRepository(get()) }

    single { BenchRepository(get()) }

    single<BenchApi> {get<Retrofit>().create(BenchApi::class.java)}

}