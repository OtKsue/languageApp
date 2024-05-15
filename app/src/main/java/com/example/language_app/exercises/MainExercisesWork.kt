package com.example.language_app.exercises

abstract class MainExercisesWork {
    protected var stripe: Int = 0
    protected var coef: Double = 0.2
    abstract fun getPoints(): Double
    fun resetStripe() {
        stripe = 0
    }
}