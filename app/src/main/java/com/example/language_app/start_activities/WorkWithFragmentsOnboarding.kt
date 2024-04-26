package com.example.language_app.start_activities

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class WorkWithFragmentsOnboarding (fragmentActivity: FragmentActivity, private val list: List<Fragment>) : FragmentStateAdapter(fragmentActivity){

    override fun createFragment(position: Int): Fragment = list[position]
    override fun getItemCount(): Int = list.size

}