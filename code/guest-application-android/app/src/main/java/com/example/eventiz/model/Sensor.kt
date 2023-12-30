package com.example.eventiz.model
import kotlinx.serialization.Serializable
@Serializable
data class Sensor(
    var id: Int,
    var type: String,
    var value: Float,
    var created_at: String,
)