package com.example.benchtalks.domain.repository

import com.example.benchtalks.domain.api.SwipeApi
import com.example.benchtalks.domain.models.*

class SwipeRepository(private val api: SwipeApi) {

    suspend fun registerUser(
        name: String,
        gender: String,
        bio: String?,
        birthday: String?,
        photos: List<String>
    ): UserResponse? {
        val request = UserRegistrationRequest(name, gender, bio, birthday, photos)
        val response = api.registerUser(request)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun getUserProfile(userId: Int): UserResponse? {
        val response = api.getUserProfile(userId)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun getSwipeFeed(userId: Int, limit: Int = 50): List<SwipeProfileResponse>? {
        val response = api.getCardsForSwipe(userId, limit)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun swipeUser(userId: Int, targetUserId: Int, isLike: Boolean): SwipeResponse? {
        val type = if (isLike) "like" else "dislike"
        val request = SwipeRequest(toUserId = targetUserId, type = type)

        val response = api.swipeUser(userId, request)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun getUserMatches(userId: Int): List<MatchResponse>? {
        val response = api.getUserMatches(userId)
        return if (response.isSuccessful) response.body() else null
    }
}