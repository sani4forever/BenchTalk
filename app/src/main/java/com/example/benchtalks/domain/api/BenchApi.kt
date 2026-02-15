package com.example.benchtalks.domain.api

import com.example.benchtalks.domain.models.BenchSuggestionResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BenchApi {
    @POST("api/v1/matches/{match_id}/suggest-benches")
    suspend fun getBenches(
        @Path("match_id") matchId: Int,
        @Query("user_id") userId: Int,
        @Query("limit") limit: Int = 10
    ): Response<List<BenchSuggestionResponse>>

    @POST("api/v1/matches/{match_id}/suggest-benches-coordinates")
    suspend fun getBenchesWithCoordinates(
        @Path("match_id") matchId: Int,
        @Query("lat1") lat1: Double,
        @Query("lon1") lon1: Double,
        @Query("lat2") lat2: Double,
        @Query("lon2") lon2: Double,
        @Query("limit") limit: Int = 10
    ): Response<List<BenchSuggestionResponse>>

}
