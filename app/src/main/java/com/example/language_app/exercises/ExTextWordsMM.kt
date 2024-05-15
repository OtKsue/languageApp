package com.example.language_app.exercises

import android.os.Bundle
import com.example.language_app.base_activities.ActivityBase
import com.example.language_app.databinding.ActTextWordsMpBinding

class ExTextWordsMM : ActivityBase<ActTextWordsMpBinding>() {

    override val screenBinding: ActTextWordsMpBinding by lazy {
        ActTextWordsMpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(screenBinding.root)
    }
}