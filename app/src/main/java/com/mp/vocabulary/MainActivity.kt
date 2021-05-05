package com.mp.vocabulary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.mp.vocabulary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.menuBottom.setOnItemSelectedListener { id ->
            var fragment: Fragment? = null
            when(id) {
                R.id.searchMenu -> fragment = SearchFragment()
                R.id.quizMenu -> fragment = QuizFragment()
                R.id.transMenu -> fragment = TranslationFragment()
                R.id.myPageMenu -> fragment = MypageFragment()
            }
            fragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit()
            } ?: Log.d("ERROR", "Error:: can't make new fragment")
        }

        binding.menuBottom.setItemSelected(R.id.searchMenu, true)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, SearchFragment())
            .commit()

    }
}