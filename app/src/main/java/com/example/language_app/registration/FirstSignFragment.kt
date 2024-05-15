package com.example.language_app.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.language_app.databinding.FrgmSignup1Binding

class FirstSignFragment : Fragment() {
    private lateinit var binding: FrgmSignup1Binding
    fun getFirstName(): String = binding.inputFirstNameEditText.text.toString()

    fun getSecondName(): String = binding.inputSecondNameEditText.text.toString()

    fun getEmail(): String = binding.inputEmailEditText.text.toString()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FrgmSignup1Binding.inflate(inflater, container, false)
        return binding.root
    }
}