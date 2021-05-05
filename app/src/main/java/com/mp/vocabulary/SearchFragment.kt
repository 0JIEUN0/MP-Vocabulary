package com.mp.vocabulary

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.mp.vocabulary.databinding.FragmentSearchBinding
import com.mp.vocabulary.viewmodel.VocaViewModel

class SearchFragment : Fragment() {
    var binding: FragmentSearchBinding? = null
    val vocaViewModel: VocaViewModel by activityViewModels()
    private val TAG = "SEARCH FRAGMENT"

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
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
                var str = ""
                if(result.isEmpty()){
                    str = "검색 결과가 없습니다."
                }
                else {
                    str = "$searchWord\n"
                    if(result.size != 1) {
                        Log.e(TAG, "Warn: Searching English word must find EXACTLY one or 0!")
                    }
                    result[0].kor.forEach {
                        str += "- $it \n"
                    }
                }
                binding!!.searchResult.text = str
            }
        } ?: Log.e(TAG, "Error: Binding is null")
    }
}