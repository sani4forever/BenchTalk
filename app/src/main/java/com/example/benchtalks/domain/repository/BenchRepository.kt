package com.example.benchtalks.domain.repository

import com.example.benchtalks.domain.api.BenchApi
import com.example.benchtalks.domain.models.BenchSuggestionResponse

class BenchRepository(val benchApi: BenchApi) {
    suspend fun suggestBenches(matchId: Int, userId: Int): Result<List<BenchSuggestionResponse>> {
        return runCatching {
            val response = benchApi.suggestBenches(matchId, userId)
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Response body is null")
            } else {
                throw Exception("Request failed with code ${response.code()}")
            }
        }
    }
}