package com.example.benchtalks.domain.repository

import com.example.benchtalks.domain.api.BenchApi
import com.example.benchtalks.domain.models.BenchSuggestionResponse

class BenchRepository(val benchApi: BenchApi) {
    suspend fun getBenches(matchId: Int, userId: Int): Result<List<BenchSuggestionResponse>> {
        return runCatching {
            val response = benchApi.getBenches(matchId, userId)
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Response body is null")
            } else {
                throw Exception("Request failed with code ${response.code()}")
            }
        }
    }

    suspend fun getBenchesWithCoordinates(
        matchId: Int,
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Result<List<BenchSuggestionResponse>> {
        return runCatching {
            val response = benchApi.getBenchesWithCoordinates(matchId, lat1, lon1, lat2, lon2)
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Response body is null")
            } else {
                throw Exception("Request failed with code ${response.code()}")
            }
        }
    }
}