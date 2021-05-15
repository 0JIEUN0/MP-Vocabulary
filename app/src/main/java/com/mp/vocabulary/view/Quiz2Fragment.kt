package com.mp.vocabulary.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mp.vocabulary.databinding.FragmentQuiz2Binding

class Quiz2Fragment : Fragment() {

    var binding: FragmentQuiz2Binding? = null

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQuiz2Binding.inflate(layoutInflater, container, false)
        return binding!!.root
    }
}