package com.example.language_app.exercises

abstract class Main_Exercises_Work {
    protected var streak: Int = 0
    protected var coef: Double = 0.2
    abstract fun getPoints(): Double
    fun resetStreak() {
        streak = 0
    }
}