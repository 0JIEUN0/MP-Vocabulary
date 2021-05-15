package com.mp.vocabulary.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.mp.vocabulary.R
import com.mp.vocabulary.databinding.ActivityMainBinding
import com.mp.vocabulary.viewmodel.VocaViewModel

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val viewModel: VocaViewModel by viewModels()
    var fragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.fragmentRequest.observe(this) {
            //fragment = when(it){ }
        }
    }

    private fun init() {
        binding.menuBottom.setOnItemSelectedListener { id ->
            when(id) {
                R.id.searchMenu -> fragment = SearchFragment()
                R.id.quizMenu -> fragment = StudyFragment()
                R.id.transMenu -> fragment = TranslationFragment()
                R.id.myPageMenu -> fragment = MypageFragment()
            }
            fragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, it)
                    .commit()
            } ?: Log.d("ERROR", "Error:: can't make new fragment")
        }

        binding.menuBottom.setItemSelected(R.id.searchMenu, true)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, SearchFragment())
            .commit()

    }
}