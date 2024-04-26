package com.example.language_app.exercises

import android.os.Bundle
import com.example.language_app.base_activities.ActivityBase
import com.example.language_app.databinding.ActAuditionBinding
class ex_audition : ActivityBase<ActAuditionBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(screenBinding.root)
    }
    override val screenBinding: ActAuditionBinding by lazy {
        ActAuditionBinding.inflate(layoutInflater)
    }
}