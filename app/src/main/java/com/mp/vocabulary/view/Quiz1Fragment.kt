package com.mp.vocabulary.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mp.vocabulary.databinding.FragmentQuiz1Binding

class Quiz1Fragment : Fragment() {
    var binding: FragmentQuiz1Binding? = null

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQuiz1Binding.inflate(layoutInflater, container, false)
        return binding!!.root
    }
}