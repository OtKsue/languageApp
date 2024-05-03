package com.example.language_app.databases

data class OneWord(
    val word: String,
    var isSelected: Boolean = false,
    var isWrong: Boolean = false,
    var isCorrect: Boolean = false,
)
