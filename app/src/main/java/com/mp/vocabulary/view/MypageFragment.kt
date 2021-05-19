package com.mp.vocabulary.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.forEach
import androidx.core.view.marginLeft
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.mp.vocabulary.data.Voca
import com.mp.vocabulary.databinding.FragmentMypageBinding
import com.mp.vocabulary.viewmodel.UserViewModel
import com.mp.vocabulary.viewmodel.VocaViewModel
import java.io.PrintStream

class MypageFragment : Fragment() {
    var binding: FragmentMypageBinding? = null
    val viewModel: VocaViewModel by activityViewModels()
    val userViewModel: UserViewModel by activityViewModels()

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMypageBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        binding!!.apply {
            if (userViewModel.userId  == null){
                addUserTextView.visibility = View.VISIBLE
            } else {
                addUserTextView.visibility = View.GONE
            }
            userId.text = userViewModel.userId ?: ""
            userRegisterLayout.visibility = View.GONE
            addUserBtn.isEnabled = false

            inputTextUserId.addTextChangedListener {
                if(it.toString().matches(Regex("^[a-zA-Z0-9ㄱ-ㅎ가-힣]+\$"))){
                    inputTextUserIdLayout.error = ""
                    addUserBtn.isEnabled = true
                } else if(it.toString() == ""){
                    addUserBtn.isEnabled = false
                } else {
                    inputTextUserIdLayout.error = "한글, 영문자, 숫자만 가능합니다."
                    addUserBtn.isEnabled = false
                }
            }

            addUserBtn.setOnClickListener {
                val inputUserId = inputTextUserId.text.toString()
                userViewModel.initUser(inputUserId)
                userId.text = inputUserId
                // 레이아웃 감추기
                userRegisterLayout.visibility = View.GONE
            }

            addUserTextView.setOnClickListener {
                it.visibility = View.GONE
                userRegisterLayout.visibility = View.VISIBLE
            }

            addVocaLayout.visibility = View.GONE
            // 단어 등록
            addVocaTextView.setOnClickListener {
                addVocaLayout.visibility = if(addVocaLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            addVocaBtn.setOnClickListener {
                val eng = addVocaEng.text.toString()
                if(eng == "") {
                    Toast.makeText(requireContext(), "등록할 단어를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    val meaningList = mutableListOf<String>()
                    korEditTexts.forEach {
                        val editText = it as EditText
                        if(editText.text.toString() != "") {
                            meaningList.add(editText.text.toString())
                        }
                    }
                    if(meaningList.size == 0) {
                        Toast.makeText(requireContext(), "등록할 단어를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        if(viewModel.engs.contains(eng)) {
                            clearAddVocaView()
                            Toast.makeText(requireContext(), "이미 등록된 단어입니다.", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            // 단어 등록하기
                            clearAddVocaView()
                            val output = PrintStream(requireActivity().openFileOutput("out.txt", Context.MODE_APPEND))
                            viewModel.addUserVoca(output, Voca(
                                    eng = eng,
                                    kor = meaningList
                            ))
                            Toast.makeText(requireContext(), "단어 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            addKorEditTextBtn.setOnClickListener {
                val editText: EditText = EditText(requireActivity())
                editText.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                editText.hint = "뜻"
                korEditTexts.addView(editText)
            }
        }
    }

    private fun clearAddVocaView() {
        binding!!.apply {
            addVocaEng.setText("")
            korEditTexts.removeAllViews()
            val editText: EditText = EditText(requireActivity())
            editText.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            editText.hint = "뜻"
            korEditTexts.addView(editText)
        }
    }
}