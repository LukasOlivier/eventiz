package com.example.eventiz.model
import kotlinx.serialization.Serializable



@Serializable
data class Emergency(
    var location: String,
    var type: String
)