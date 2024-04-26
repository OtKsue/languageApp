package com.example.language_app.language_selection

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.language_app.base_activities.ActivityBase
import com.example.language_app.registration.LoginActivity
import com.example.language_app.databinding.ActLanguageChoiceBinding
import com.example.language_app.databinding.ActLanguageChoiceBinding.inflate
import com.example.language_app.util.setLocale
import com.example.language_app.base_activities.MainActivity

class ActivityLanguage : ActivityBase<ActLanguageChoiceBinding>() {

    override val screenBinding: ActLanguageChoiceBinding by lazy {
        inflate(layoutInflater)
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(screenBinding.root)

        val languages = listOf(
            DataLanguageCapture("Russian"),
            DataLanguageCapture("English"),
            DataLanguageCapture("Chinese"),
            DataLanguageCapture("Belarus"),
            DataLanguageCapture("Kazakh"),
        )

        screenBinding.rvLanguageButtons.layoutManager = LinearLayoutManager(this)
        screenBinding.rvLanguageButtons.adapter = WorkActLanguage(languages) {
            languages.forEachIndexed { index, item ->
                item.isSelectActivity = index == it
            }
            screenBinding.rvLanguageButtons.adapter?.notifyDataSetChanged()
        }

        screenBinding.btnChooseLanguage.setOnClickListener {
            var id = -1
            var name = ""
            languages.forEachIndexed { index, item ->
                if (item.isSelectActivity) {
                    name = item.name
                    id = index
                }
            }
            if (id != -1) {
                val selectedLocale = when (name) {
                    "Russian" -> "ru"
                    "English" -> "en"
                    "Chinese" -> "ch"
                    "Belarus" -> "be"
                    "Kazakh" -> "ka"
                    else -> "en"
                }
                setLocale(language = selectedLocale, context = this)
                recreate()

                val isProfileChange = intent.getBooleanExtra("ProfileChange", false)
                if (!isProfileChange)  {
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                }

                finish()
            }
        }

    }

}