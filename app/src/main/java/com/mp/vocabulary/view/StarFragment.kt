package com.mp.vocabulary.view

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mp.vocabulary.adapter.DetailVocaAdapter
import com.mp.vocabulary.data.Voca
import com.mp.vocabulary.database.DBTable
import com.mp.vocabulary.databinding.FragmentStarBinding
import com.mp.vocabulary.viewmodel.VocaViewModel
import java.util.*
import kotlin.collections.ArrayList

class StarFragment : Fragment() {

    var binding: FragmentStarBinding? = null

    val viewModel: VocaViewModel by activityViewModels()
    lateinit var tts: TextToSpeech
    val adapter: DetailVocaAdapter by lazy {
        DetailVocaAdapter(DBTable.STAR, requireContext(), ArrayList())
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
        binding = FragmentStarBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initTts()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.starWordListLiveData.observe(viewLifecycleOwner){
            adapter.setNewItems(it.toMutableList())
        }
    }

    private fun init() {
        binding!!.apply {
            binding!!.starRecyclerView.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter.itemClickListener = object: DetailVocaAdapter.OnItemClickListener{
                override fun onClickSound(holder: RecyclerView.ViewHolder, view: View, data: Voca, position: Int) {
                    tts.speak(data.eng, TextToSpeech.QUEUE_ADD, null, null)
                }
                override fun onClickStar(holder: RecyclerView.ViewHolder, view: View, data: Voca, position: Int) {
                }
                override fun onClickDelete(holder: RecyclerView.ViewHolder, view: View, data: Voca, position: Int) {
                    // 즐겨찾기에서 삭제
                    viewModel.deleteFromStar(data)
                }
            }
            binding!!.starRecyclerView.adapter = adapter
        }
    }

    private fun initTts() {
        tts = TextToSpeech(context, TextToSpeech.OnInitListener {
            tts.language = Locale.US
        })
    }
}