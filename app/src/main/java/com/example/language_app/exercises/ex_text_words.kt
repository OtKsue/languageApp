package com.example.language_app.exercises

import android.os.Bundle
import com.example.language_app.databinding.ActWordsBinding
import com.example.language_app.base_activities.ActivityBase

class ex_text_words : ActivityBase<ActWordsBinding>() {
    override val screenBinding: ActWordsBinding by lazy {
        ActWordsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenBinding.btnAction.setText("Hm")
        screenBinding.rvWords.adapter = null


        screenBinding.tvEnglishVariant.text = "Может"
        screenBinding.tvTranscription.text = "позже"

        setContentView(screenBinding.root)
    }
}