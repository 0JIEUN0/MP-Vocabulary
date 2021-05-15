package com.mp.vocabulary.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.mp.vocabulary.R
import com.mp.vocabulary.adapter.SimpleVocaAdapter
import com.mp.vocabulary.data.QuizVoca
import com.mp.vocabulary.databinding.FragmentQuiz1Binding
import com.mp.vocabulary.viewmodel.VocaViewModel
import java.util.*
import kotlin.collections.ArrayList

class Quiz1Fragment : Fragment() {
    var binding: FragmentQuiz1Binding? = null
    val viewModel: VocaViewModel by activityViewModels()
    var quizWords: MutableList<QuizVoca> = mutableListOf()
    val chipArray = intArrayOf(R.id.chip1, R.id.chip2, R.id.chip3, R.id.chip4)
    val random: Random = Random()
    var idOfCorrect: Int = -1
    var count: Int = 0
    var current: Int = 0
    var numberOfQuiz = 5 // default

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.isQuizOk.observe(viewLifecycleOwner){
            if(it) {
                quizWords = viewModel.wordListForQuiz.value!!.toMutableList()
                binding!!.chooseBtn.text = "다음"
                setQuiz(0)
            }
        }
    }

    private fun setQuiz(idx: Int) {
        idOfCorrect = chipArray[random.nextInt(4)]
        val quizVoca: QuizVoca = quizWords[idx]

        Log.d("this-->", "${quizVoca.eng}, ${quizVoca.kor}")
        binding!!.progressNum.text = "${idx+1} / $numberOfQuiz"
        binding!!.quizWord.text = quizVoca.eng
        var i = 0
        var str = quizVoca.others[i]
        binding!!.chipGroupQuiz.clearCheck()
        chipArray.forEach {
            val chip: Chip = requireView().findViewById<Chip>(it)
            if(it == idOfCorrect){
                chip.text = quizVoca.kor
            }
            else {
                chip.text = str
                if(i!=2){
                    i += 1
                    str = quizVoca.others[i]
                }
            }
        }
    }

    private fun init() {
        binding!!.apply {
            startBtn.setOnClickListener {
                numberOfQuiz = when (chipGroupNumber.checkedChipId) {
                    R.id.chip5 -> 5
                    R.id.chip10 -> 10
                    R.id.chip15 -> 15
                    else -> 5
                }
                settingLayout.visibility = View.GONE
                quizLayout.visibility = View.VISIBLE
                viewModel.makeQuiz(numberOfQuiz)
            }

            chooseBtn.setOnClickListener {
                if(chooseBtn.text == "다음"){
                    if(chipGroupQuiz.checkedChipId == View.NO_ID){
                        Toast.makeText(context, "하나를 선택해주세요.", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        userChoose()
                    }
                }
                else if(chooseBtn.text == "결과"){
                    userChoose()
                    viewModel.isQuizOk.value = false
                    quizLayout.visibility = View.GONE
                    resultLayout.visibility = View.VISIBLE
                    showResult()
                }
            }
            addNoteBtn.setOnClickListener {

            }

            finishBtn.setOnClickListener {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .remove(this@Quiz1Fragment)
                    .commit()
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun showResult() {
        binding!!.resultNum.text = "$count 개"
        if(count != numberOfQuiz) {
            binding!!.wrongRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val adapter: SimpleVocaAdapter = SimpleVocaAdapter(ArrayList())
            quizWords.forEach {
                val color: Int =
                    if(it.isCorrect) resources.getColor(R.color.green)
                    else resources.getColor(R.color.dark_red)
                adapter.addItem(it.eng, it.kor, color)
            }
            binding!!.wrongRecyclerView.adapter = adapter
            binding!!.wrongRecyclerView.visibility = View.VISIBLE
        }else {
            binding!!.wrongRecyclerView.visibility = View.GONE
            binding!!.addNoteBtn.isEnabled = false
        }
    }

    private fun userChoose() {
        var chip: Chip = requireView().findViewById<Chip>(binding!!.chipGroupQuiz.checkedChipId)
        if(chip.id == idOfCorrect){
            //chip.chipBackgroundColor = Color.GREEN
            quizWords[current].isCorrect = true
            count++
        }else{
            quizWords[current].isCorrect = false
        }
        current++

        if(current +1 == numberOfQuiz) {
            binding!!.chooseBtn.text = "결과"
            setQuiz(current)
        } else if(current != numberOfQuiz) {
            setQuiz(current)
        }
    }
}