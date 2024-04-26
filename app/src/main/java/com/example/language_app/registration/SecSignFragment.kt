package com.example.language_app.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.language_app.databinding.FragmentSignup2Binding
import com.example.language_app.databinding.FragmentSignup2Binding.inflate

class SecSignFragment : Fragment() {
    private lateinit var binding: FragmentSignup2Binding
    fun getPassword(): String = binding.inputPasswordEditText.text.toString()
    fun getConfirm(): String = binding.inputConfirmEditText.text.toString()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate(inflater, container, false)
        return binding.root
    }

}