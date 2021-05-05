package com.mp.vocabulary.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.mp.vocabulary.R
import com.mp.vocabulary.data.Voca
import java.lang.Exception
import java.util.*

class VocaViewModel(application: Application) : AndroidViewModel(application){
    private val TAG = "VocaViewModel"

    // words list [ eng : kor(meaning) = 1 : n ]
    val words: MutableList<Voca> = mutableListOf<Voca>()
    val engs: MutableList<String> = mutableListOf<String>()

    init {
        try {
            val scan2 = Scanner(application.openFileInput("out.txt"))
            readFile(scan2)
        }catch (e: Exception) {
            Log.e(TAG, "사용자가 추가한 단어 없음")
        }
        val scan1 = Scanner(application.resources.openRawResource(R.raw.words))
        readFile(scan1)

        words.forEach {
            engs.add(it.eng)
        }
    }

    fun search(target: String): List<Voca> {
        return words.filter {
            it.eng == target
        }
    }

    private fun readFile(scan: Scanner) {
        while(scan.hasNext()){
            val input = scan.nextLine()
            val splitInput = input.split(":")
            val meanings = splitInput[1].split("/")
            words.add(Voca(splitInput[0], meanings.toMutableList()))
        }
        scan.close()
    }

}