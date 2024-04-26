package com.example.language_app.start_activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.language_app.R.layout.fragment_onboarding3

class ThirdFragmentOnboard  : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(fragment_onboarding3, container, false)
}