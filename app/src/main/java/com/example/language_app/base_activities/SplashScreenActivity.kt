package com.example.language_app.base_activities


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.language_app.Application
import com.example.language_app.Application.Companion.storage
import com.example.language_app.databinding.ActivitySplashScreenBinding
import com.example.language_app.databinding.ActivitySplashScreenBinding.inflate
import com.example.language_app.activities.language.LanguageActivity
import com.example.language_app.activities.welcome.OnboardingActivity
import kotlinx.coroutines.launch


class SplashScreenActivity : ActivityBase<ActivitySplashScreenBinding>() {
    override val screenBinding: ActivitySplashScreenBinding by lazy {
        inflate(layoutInflater)
    }

    private var currentFragment: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(screenBinding.root)

        val savedLanguage = loadLanguagePreference(this@SplashScreenActivity)
        setLocale(savedLanguage, this@SplashScreenActivity)

        currentFragment = storage.getInt("OnboardingFragment")

        if (currentFragment != -1) {
            startActivity(Intent(this@SplashScreenActivity, OnboardingActivity::class.java))
        } else {
            lifecycleScope.launch {
                val hasSession = (application as Application).hasSavedSession()
                if (hasSession) startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                else startActivity(Intent(this@SplashScreenActivity, LanguageActivity::class.java))
            }
        }
        finish()
    }
    private fun loadLanguagePreference(context: Context): String = storage.getString("language")
}