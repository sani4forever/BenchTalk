package com.example.benchtalks.domain.api


import com.example.benchtalks.domain.models.MatchResponse
import com.example.benchtalks.domain.models.SwipeProfileResponse
import com.example.benchtalks.domain.models.SwipeRequest
import com.example.benchtalks.domain.models.SwipeResponse
import com.example.benchtalks.domain.models.UserRegistrationRequest
import com.example.benchtalks.domain.models.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface SwipeApi {

    @POST("/api/v1/users")
    suspend fun registerUser(
        @Body request: UserRegistrationRequest
    ): Response<UserResponse>

    @GET("/api/v1/users/{user_id}")
    suspend fun getUserProfile(
        @Path("user_id") userId: Int
    ): Response<UserResponse>

    @GET("/api/v1/users/{user_id}/discover")
    suspend fun getCardsForSwipe(
        @Path("user_id") userId: Int,
        @Query("limit") limit: Int = 50
    ): Response<List<SwipeProfileResponse>>

    @POST("/api/v1/users/{user_id}/swipe")
    suspend fun swipeUser(
        @Path("user_id") userId: Int,
        @Body request: SwipeRequest
    ): Response<SwipeResponse>

    @GET("/api/v1/users/{user_id}/matches")
    suspend fun getUserMatches(
        @Path("user_id") userId: Int
    ): Response<List<MatchResponse>>
}