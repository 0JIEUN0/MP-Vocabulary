package com.mp.vocabulary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mp.vocabulary.databinding.FragmentTranslationBinding
import com.mp.vocabulary.server.RetrofitServerManage
import com.mp.vocabulary.server.data.CODE
import com.mp.vocabulary.server.data.TranslationResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TranslationFragment : Fragment() {
    var binding: FragmentTranslationBinding? = null
    val scope = CoroutineScope(Dispatchers.Main)
    val TAG = "TranslationFragment"
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

        if(!RetrofitServerManage.initRetrofit()){
            Log.e(TAG, "Server init failed...")
        }

        init()
    }

    private fun init(){
        binding!!.apply {
            spinner.prompt = "언어 선택"
            spinner.adapter = context?.let {
                ArrayAdapter<String>(
                    it,
                    android.R.layout.simple_dropdown_item_1line,
                    arrayListOf("한국어", "영어")
                )
            }

            transBtn.setOnClickListener {
                if(inputText.text.toString() == ""){
                    Toast.makeText(context, "번역할 문장이 없습니다.", Toast.LENGTH_LONG).show()
                } else {
                    val sourceCode: CODE = if(spinner.selectedItem.toString() == "한국어") CODE.ko else CODE.en
                    val targetCode: CODE = if(sourceCode == CODE.ko) CODE.en else CODE.ko
                    translation(
                        sourceCode = sourceCode.toString(),
                        targetCode = targetCode.toString(),
                        str = inputText.text.toString()
                    )
                }
            }
        }
    }

    private fun translation(sourceCode: String, targetCode: String, str: String) {
        binding!!.progressBar.visibility = View.VISIBLE
        scope.launch {
            val headers: HashMap<String, Any> = HashMap()
            with(headers) {
                //put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                put("X-Naver-Client-Id", BuildConfig.PAPAGO_ID)
                put("X-Naver-Client-Secret", BuildConfig.PAPAGO_SECREAT)
            }
            var response: TranslationResponse? = null
            withContext(Dispatchers.IO) {
                response = RetrofitServerManage.translation(
                    headers = headers,
                    source = sourceCode,
                    target = targetCode,
                    text = str
                )
            }
            binding!!.progressBar.visibility = View.GONE
            binding!!.transResultText.text = response?.translatedText
        }
    }
}