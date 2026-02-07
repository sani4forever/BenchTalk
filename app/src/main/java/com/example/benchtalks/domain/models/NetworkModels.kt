package com.example.benchtalks.domain.models

import com.google.gson.annotations.SerializedName


data class UserRegistrationRequest(
    @SerializedName("name") val name: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("bio") val bio: String?,
    @SerializedName("birthday") val birthday: String?,
    @SerializedName("photos") val photos: List<String>?
)

data class UserResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("bio") val bio: String,
    @SerializedName("birthday") val birthday: String,
    @SerializedName("age") val age: Int,
    @SerializedName("photos") val photos: List<String>,
    @SerializedName("created_at") val createdAt: String
)

data class SwipeProfileResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("age") val age: Int,
    @SerializedName("gender") val gender: String,
    @SerializedName("bio") val bio: String,
    @SerializedName("photos") val photos: List<String>,
    @SerializedName("distance_km") val distanceKm: Int
)

data class SwipeRequest(
    @SerializedName("to_user_id") val toUserId: Int,
    @SerializedName("type") val type: String
)

data class SwipeResponse(
    @SerializedName("swipe_id") val swipeId: Int,
    @SerializedName("is_match") val isMatch: Boolean,
    @SerializedName("match_id") val matchId: Int?
)

data class MatchResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("user") val user: MatchUserPreview,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("unread_messages_count") val unreadMessagesCount: Int
)

data class MatchUserPreview(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("age") val age: Int,
    @SerializedName("primary_photo_url") val primaryPhotoUrl: String?
)