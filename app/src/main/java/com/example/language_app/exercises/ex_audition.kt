package com.example.language_app.exercises

import android.os.Bundle
import com.example.language_app.base_activities.ActivityBase
import com.example.language_app.databinding.ActivityAuditionExerciseBinding
class ex_audition : ActivityBase<ActivityAuditionExerciseBinding>{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(screenBinding.root)
    }
    override val screenBinding: ActivityAuditionExerciseBinding by lazy {
        ActivityAuditionExerciseBinding.inflate(layoutInflater)
    }
}