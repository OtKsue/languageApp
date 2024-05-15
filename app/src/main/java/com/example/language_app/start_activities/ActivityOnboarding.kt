package com.example.language_app.start_activities

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat.getColor
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.language_app.base_activities.ActivityBase
import com.example.language_app.databases.Initialization.Companion.storage
import com.example.language_app.language_selection.ActivityLanguage
import com.example.language_app.R
import com.example.language_app.R.string.onboarding_next_btn
import com.example.language_app.R.string.onboarding_more_btn
import com.example.language_app.R.string.onboarding_select_btn
import com.example.language_app.databinding.ActOnboardingMainBinding
import com.example.language_app.databinding.ActOnboardingMainBinding.inflate

class ActivityOnboarding : ActivityBase<ActOnboardingMainBinding>() {

    private val onboardingFragments = listOf(
        FirstFragmentOnboard(),
        SecondFragmentOnboard(),
        ThirdFragmentOnboard(),
    )

    private var currentFragment: Int = 0

    override val screenBinding: ActOnboardingMainBinding by lazy {
        inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val btnText = listOf(
            getString(onboarding_next_btn),
            getString(onboarding_more_btn),
            getString(onboarding_select_btn),
        )

        currentFragment = storage.getInt("OnboardingFragment")
        setUIById(currentFragment, btnText[currentFragment])
        val adapter = WorkWithFragmentsOnboarding(this, onboardingFragments)

        with(screenBinding.vpOnboarding) {
            this.adapter = adapter
            currentItem = currentFragment
            registerOnPageChangeCallback(object :
                OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    currentFragment = position
                    storage.saveInt("OnboardingFragment", currentFragment)
                    setUIById(currentFragment, btnText[currentFragment])
                }
            })
        }

        screenBinding.btnOnboardingNext.setOnClickListener {
            if (currentFragment < 2) {
                screenBinding.vpOnboarding.currentItem = ++currentFragment
                storage.saveInt("OnboardingFragment", currentFragment)
                setUIById(currentFragment, btnText[currentFragment])
            } else {
                storage.saveInt("OnboardingFragment", -1)
                startActivity(Intent(this, ActivityLanguage::class.java))
                finish()
            }
        }

        screenBinding.tvSkip.setOnClickListener {
            currentFragment = -1
            storage.saveInt("OnboardingFragment", currentFragment)
            startActivity(Intent(this, ActivityLanguage::class.java))
            finish()
        }

        setContentView(screenBinding.root)
    }

    private fun setUIById(id: Int, btnText: String) {
        screenBinding.btnOnboardingNext.text = btnText
        when (id) {
            0 -> {
                screenBinding.ivCircle1.setBackgroundColor(
                    getColor(
                        this,
                        R.color.circle_active
                    )
                )
                screenBinding.ivCircle2.setBackgroundColor(
                    getColor(
                        this,
                        R.color.circle_inactive
                    )
                )
                screenBinding.ivCircle3.setBackgroundColor(
                    getColor(
                        this,
                        R.color.circle_inactive
                    )
                )
            }

            1 -> {
                screenBinding.ivCircle1.setBackgroundColor(
                    getColor(
                        this,
                        R.color.circle_inactive
                    )
                )
                screenBinding.ivCircle2.setBackgroundColor(
                    getColor(
                        this,
                        R.color.circle_active
                    )
                )
                screenBinding.ivCircle3.setBackgroundColor(
                    getColor(
                        this,
                        R.color.circle_inactive
                    )
                )
            }

            2 -> {
                screenBinding.ivCircle1.setBackgroundColor(
                    getColor(
                        this,
                        R.color.circle_inactive
                    )
                )
                screenBinding.ivCircle2.setBackgroundColor(
                    getColor(
                        this,
                        R.color.circle_inactive
                    )
                )
                screenBinding.ivCircle3.setBackgroundColor(
                    getColor(
                        this,
                        R.color.circle_active
                    )
                )
            }
        }
    }
}