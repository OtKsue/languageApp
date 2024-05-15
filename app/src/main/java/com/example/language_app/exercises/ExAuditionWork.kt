package com.example.language_app.exercises

class ExAuditionWork : MainExercisesWork() {

    init {
        coef = 2.0
        stripe = 0
    }

    override fun getPoints(): Double {
        stripe++
        return if (stripe > 1) {
            1.0 + coef * stripe
        } else 1.0
    }
}