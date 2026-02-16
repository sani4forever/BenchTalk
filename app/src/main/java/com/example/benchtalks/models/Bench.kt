package com.example.benchtalks.models

data class Bench(
    val id: String,
    val type: String,
    val latitude: Double,
    val longitude: Double,
    val distanceUserA: Double,
    val distanceUserB: Double,
    val totalDistance: Double,
    val score: Double,
    val address: String? = null,
    val tags: Map<String, String> = emptyMap()
)