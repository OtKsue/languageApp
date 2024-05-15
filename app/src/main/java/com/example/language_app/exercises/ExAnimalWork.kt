package com.example.language_app.exercises
class ExAnimalWork : MainExercisesWork() {

    init {
        coef = 0.2
        stripe = 0
    }

    override fun getPoints(): Double {
        stripe++
        return if (stripe > 1) {
            1.0 + coef * stripe
        } else 1.0
    }
}