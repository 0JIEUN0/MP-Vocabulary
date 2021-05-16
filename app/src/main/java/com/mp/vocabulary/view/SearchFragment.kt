package com.mp.vocabulary.view

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.mp.vocabulary.databinding.FragmentSearchBinding
import com.mp.vocabulary.viewmodel.VocaViewModel
import java.util.*

class SearchFragment : Fragment() {
    var binding: FragmentSearchBinding? = null
    val viewModel: VocaViewModel by activityViewModels()
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
        initViewModel()
    }

    private fun initTts() {
        tts = TextToSpeech(context, TextToSpeech.OnInitListener {
            isTtsReady = true;
            tts.language = Locale.US
        })
    }

    private fun init() {
        binding!!.apply {
            inputSearch.setAdapter(
                    ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            viewModel.engs)
            )

            searchBtn.setOnClickListener {
                val searchWord = binding!!.inputSearch.text.toString()
                viewModel.search(searchWord)
            }

            readWordBtn.setOnClickListener {
                tts.speak(binding!!.searchResultWord.text, TextToSpeech.QUEUE_ADD, null, null)
            }

            addStarBtn.setOnClickListener {
                // 즐겨 찾기에 등록
                if(viewModel.insertToStar(binding!!.searchResultWord.text.toString())){
                    Toast.makeText(context, "즐겨찾기에 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "이미 즐겨찾기에 등록된 단어입니다.", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun initViewModel() {
        viewModel.apply {
            searchResultWord.observe(viewLifecycleOwner) {
                binding!!.searchResultWord.text = it
            }
            searchResultMeaning.observe(viewLifecycleOwner) {
                binding!!.searchResultMeaning.text = it
            }
            isSearchFind.observe(viewLifecycleOwner) {
                if (it) {
                    binding!!.readWordBtn.visibility = View.VISIBLE
                    binding!!.addStarBtn.visibility = View.VISIBLE
                }
                else {
                    binding!!.readWordBtn.visibility = View.GONE
                    binding!!.addStarBtn.visibility = View.GONE
                }
            }
        }
    }

}