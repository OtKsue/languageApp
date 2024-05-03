package com.example.language_app.databases

import kotlinx.serialization.SerialName

data class Words(
    val id: Int,
    @SerialName("eng_variant")
    val englishVariant: String,
    @SerialName("transcription")
    val transcription: String,
    @SerialName("variant_1")
    val variant1: String,
    @SerialName("variant_2")
    val variant2: String,
    @SerialName("variant_3")
    val variant3: String,
    @SerialName("variant_4")
    val variant4: String,
    @SerialName("correct")
    val correct: String,
)
