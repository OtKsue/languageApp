package com.example.language_app.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.language_app.databinding.FragmentSignup1Binding
import com.example.language_app.databinding.FragmentSignup1Binding.inflate

class FirstSignFragment : Fragment() {
    private lateinit var binding: FragmentSignup1Binding
    fun getFirstName(): String = binding.inputFirstNameEditText.text.toString()

    fun getSecondName(): String = binding.inputSecondNameEditText.text.toString()

    fun getEmail(): String = binding.inputEmailEditText.text.toString()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignup1Binding.inflate(inflater, container, false)
        return binding.root
    }
}