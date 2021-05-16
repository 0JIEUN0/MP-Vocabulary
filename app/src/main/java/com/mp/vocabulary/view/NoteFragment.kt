package com.mp.vocabulary.view

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mp.vocabulary.adapter.DetailVocaAdapter
import com.mp.vocabulary.data.Voca
import com.mp.vocabulary.databinding.FragmentNoteBinding
import com.mp.vocabulary.viewmodel.VocaViewModel
import java.util.*
import kotlin.collections.ArrayList

class NoteFragment : Fragment() {
    var binding: FragmentNoteBinding? = null
    val viewModel: VocaViewModel by activityViewModels()
    lateinit var tts: TextToSpeech
    val adapter: DetailVocaAdapter by lazy {
        DetailVocaAdapter(requireContext(), ArrayList())
    }

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
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNoteBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initTts()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.noteWordListLiveData.observe(viewLifecycleOwner){
            adapter.setNewItems(it.toMutableList())
        }
    }

    private fun init() {
        binding!!.apply {
            binding!!.noteRecyclerView.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter.itemClickListener = object: DetailVocaAdapter.OnItemClickListener{
                override fun onClickSound(holder: RecyclerView.ViewHolder, view: View, data: Voca, position: Int) {
                    tts.speak(data.eng, TextToSpeech.QUEUE_ADD, null, null)
                }
                override fun onClickStar(holder: RecyclerView.ViewHolder, view: View, data: Voca, position: Int) {
                    // 즐겨 찾기에 등록
                    if(viewModel.insertToStar(data)){
                        Toast.makeText(context, "즐겨찾기에 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "이미 즐겨찾기에 등록된 단어입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            binding!!.noteRecyclerView.adapter = adapter
        }
    }

    private fun initTts() {
        tts = TextToSpeech(context, TextToSpeech.OnInitListener {
            tts.language = Locale.US
        })
    }
}