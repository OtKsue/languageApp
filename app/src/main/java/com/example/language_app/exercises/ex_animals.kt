package com.example.language_app.exercises

import android.os.Bundle
import com.example.language_app.base_activities.ActivityBase
import com.example.language_app.databinding.ActAnimalBinding
class ex_animals : ActivityBase<ActAnimalBinding>() {

    override val screenBinding: ActAnimalBinding by lazy {
        ActAnimalBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(screenBinding.root)
    }
}