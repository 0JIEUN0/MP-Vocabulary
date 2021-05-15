package com.mp.vocabulary.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mp.vocabulary.databinding.FragmentStudyBinding

class StudyFragment : Fragment() {
    var binding: FragmentStudyBinding? = null

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStudyBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.apply {
            quiz1.setOnClickListener {
                // 주관식 퀴즈
            }

            quiz2.setOnClickListener {
                // 듣기 평가
            }

            wrongNote.setOnClickListener {
                // 오답노트
            }

            star.setOnClickListener {
                // 즐겨찾기
            }
        }
    }
}