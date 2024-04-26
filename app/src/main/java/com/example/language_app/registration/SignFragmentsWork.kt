package com.example.language_app.registration

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
class SignFragmentsWork(fragmentActivity: FragmentActivity, private val list: List<Fragment>) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment = list[position]
    override fun getItemCount(): Int = list.size
    fun getFragmentByPosition(position: Int) : Fragment = list[position]
}