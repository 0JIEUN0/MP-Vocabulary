package com.mp.vocabulary.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mp.vocabulary.databinding.FragmentTranslationBinding
import com.mp.vocabulary.server.data.CODE
import com.mp.vocabulary.viewmodel.VocaViewModel

class TranslationFragment : Fragment() {
    var binding: FragmentTranslationBinding? = null
    val TAG = "TranslationFragment"
    val viewModel: VocaViewModel by activityViewModels()

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTranslationBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.translatedString.observe(viewLifecycleOwner) {
            binding!!.progressBar.visibility = View.GONE
            binding!!.transResultText.text = it
        }
    }

    private fun init(){
        binding!!.apply {
            toggleBtn.setOnCheckedChangeListener { button, isChecked ->
                toggleBtn2.isChecked = isChecked
            }

            transBtn.setOnClickListener {
                if(inputText.text.toString() == ""){
                    Toast.makeText(context, "번역할 문장이 없습니다.", Toast.LENGTH_LONG).show()
                } else {
                    binding!!.progressBar.visibility = View.VISIBLE
                    val sourceCode: CODE = if(toggleBtn.isChecked) CODE.en else CODE.ko
                    val targetCode: CODE = if(sourceCode == CODE.ko) CODE.en else CODE.ko
                    viewModel.translation(
                        sourceCode = sourceCode.toString(),
                        targetCode = targetCode.toString(),
                        str = inputText.text.toString()
                    )
                }
            }
        }
    }
}