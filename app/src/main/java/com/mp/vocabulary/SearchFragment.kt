package com.mp.vocabulary

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.mp.vocabulary.databinding.FragmentSearchBinding
import com.mp.vocabulary.viewmodel.VocaViewModel
import java.util.*

class SearchFragment : Fragment() {
    var binding: FragmentSearchBinding? = null
    val vocaViewModel: VocaViewModel by activityViewModels()
    private val TAG = "SEARCH FRAGMENT"
    lateinit var tts: TextToSpeech
    var isTtsReady = false

    override fun onStop() {
        super.onStop()
        tts.stop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        tts.shutdown()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTts()
        init()
    }

    private fun initTts() {
        tts = TextToSpeech(context, TextToSpeech.OnInitListener {
            isTtsReady = true;
            tts.language = Locale.US
        })
    }

    fun init() {
        binding?.let {
            it.inputSearch.setAdapter(
                    ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            vocaViewModel.engs)
            )

            it.searchBtn.setOnClickListener {
                val searchWord = binding!!.inputSearch.text.toString()
                val result = vocaViewModel.search(searchWord)
                var resultWord = searchWord
                var resultMeaning = ""
                if(result.isEmpty()){
                    resultMeaning = "검색 결과가 없습니다."
                    binding!!.readWordBtn.visibility = View.GONE
                }
                else {
                    if(result.size != 1) {
                        resultMeaning = "검색 결과가 없습니다."
                        Log.e(TAG, "Warn: Searching English word must find EXACTLY one or 0!")
                        binding!!.readWordBtn.visibility = View.GONE
                    }
                    else {
                        binding!!.readWordBtn.visibility = View.VISIBLE
                        result[0].kor.forEach {
                            resultMeaning += "✅✅ $it \n"
                        }
                    }
                }

                binding!!.searchResultWord.text = resultWord
                binding!!.searchResultMeaning.text = resultMeaning
            }

            it.readWordBtn.setOnClickListener {
                tts.speak(binding!!.searchResultWord.text, TextToSpeech.QUEUE_ADD, null, null)
            }

        } ?: Log.e(TAG, "Error: Binding is null")
    }
}