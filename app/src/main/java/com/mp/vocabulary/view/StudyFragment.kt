package com.mp.vocabulary.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mp.vocabulary.databinding.FragmentStudyBinding
import com.mp.vocabulary.viewmodel.FragmentRequest
import com.mp.vocabulary.viewmodel.VocaViewModel

class StudyFragment : Fragment() {
    var binding: FragmentStudyBinding? = null
    val viewModel: VocaViewModel by activityViewModels()

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
                // 단어 맞추기
                viewModel.fragmentTranslationRequest(FragmentRequest.REQUEST_QUIZ1)
            }

            quiz2.setOnClickListener {
                // 듣기 평가
                viewModel.fragmentTranslationRequest(FragmentRequest.REQUEST_QUIZ2)
            }

            wrongNote.setOnClickListener {
                // 오답노트
                viewModel.fragmentTranslationRequest(FragmentRequest.REQUEST_NOTE)
            }

            star.setOnClickListener {
                // 즐겨찾기
                viewModel.fragmentTranslationRequest(FragmentRequest.REQUEST_STAR)
            }
        }
    }
}