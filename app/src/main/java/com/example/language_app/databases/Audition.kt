package com.example.language_app.databases

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Audition(
    val id: Int,
    @SerialName("word")
    val word: String,
    @SerialName("transcription")
    val transcription: String,
)
