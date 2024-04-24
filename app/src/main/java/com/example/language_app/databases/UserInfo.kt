package com.example.language_app.databases

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserInfo {
    val id: String,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("second_name")
    val secondName: String,
    @SerialName("email")
    val userEmail: String,
    @SerialName("photo_url")
    val userPhotoUrl: String? = null,
    val points: Double = 0.0,
}