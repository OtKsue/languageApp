package com.example.language_app.base_activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.language_app.databases.Initialization
import com.example.language_app.databases.Initialization.Companion.storage
import com.example.language_app.databinding.ActSplashScreenBinding
import com.example.language_app.databinding.ActSplashScreenBinding.inflate
import com.example.language_app.language_selection.ActivityLanguage
import com.example.language_app.start_activities.ActivityOnboarding
import kotlinx.coroutines.launch
import com.example.language_app.util.setLocale

class SplashScreenActivity : ActivityBase<ActSplashScreenBinding>() {
    override val screenBinding: ActSplashScreenBinding by lazy {
        inflate(layoutInflater)
    }

    private var currentFragment: Int = 0,

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(screenBinding.root)

        val savedLanguage = loadLanguagePreference(this@SplashScreenActivity)
        setLocale(savedLanguage, this@SplashScreenActivity)

        currentFragment = storage.getInt("OnboardingFragment")

        if (currentFragment != -1) {
            startActivity(Intent(this@SplashScreenActivity, ActivityOnboarding::class.java))
        } else {
            lifecycleScope.launch {
                val hasSession = (application as Initialization).hasSavedSession()
                if (hasSession) startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                else startActivity(Intent(this@SplashScreenActivity, ActivityLanguage::class.java))
            }
        }
        finish()
    }
    private fun loadLanguagePreference(context: Context): String = storage.getString("language")
}