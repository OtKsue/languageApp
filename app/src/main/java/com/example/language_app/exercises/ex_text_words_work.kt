package com.example.language_app.exercises

class ex_text_words_work : Main_Exercises_Work() {
    init {
        coef = 0.2
        streak = 0
    }

    override fun getPoints(): Double {
        streak++
        return if (streak > 1) {
            1.0 + coef * streak
        } else 1.0
    }
}