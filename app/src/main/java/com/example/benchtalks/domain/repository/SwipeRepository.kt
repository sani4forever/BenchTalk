package com.example.benchtalks.domain.repository

import com.example.benchtalks.domain.api.SwipeApi
import com.example.benchtalks.domain.models.*

class SwipeRepository(private val api: SwipeApi) {

    suspend fun registerUser(
        name: String,
        gender: String,
        bio: String?,
        age: Int?,
        email: String?,
        photos: List<String>
    ): Result<UserResponse> {
        return runCatching {
            val request = UserRegistrationRequest(name, gender, bio, age, photos, email)
            val response = api.registerUser(request)
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Пустое тело ответа")
            } else {
                throw Exception("Ошибка сервера: ${response.code()}")
            }
        }
    }

    suspend fun getSwipeFeed(userId: Int, limit: Int = 50): Result<List<SwipeProfileResponse>> {
        return runCatching {
            val response = api.getCardsForSwipe(userId, limit)
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Пустое тело ответа")
            } else {
                throw Exception("Ошибка сервера: ${response.code()}")
            }
        }
    }

    suspend fun swipeUser(userId: Int, targetUserId: Int, isLike: Boolean): Result<SwipeResponse> {
        return runCatching {
            val type = if (isLike) "like" else "dislike"
            val request = SwipeRequest(toUserId = targetUserId, type = type)

            val response = api.swipeUser(userId, request)
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Пустое тело ответа")
            } else {
                throw Exception("Ошибка сервера: ${response.code()}")
            }
        }
    }

    suspend fun getUserMatches(userId: Int): List<MatchResponse>? {
        val response = api.getUserMatches(userId)
        return if (response.isSuccessful) response.body() else null
    }


    suspend fun getUserProfile(userId: Int): Result<UserResponse> {
        return runCatching {
            val response = api.getUserProfile(userId)
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Пустое тело ответа")
            } else {
                throw Exception("Ошибка сервера: ${response.code()}")
            }
        }
    }
}