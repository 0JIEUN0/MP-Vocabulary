package com.mp.vocabulary.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.mp.vocabulary.R
import com.mp.vocabulary.data.Voca
import java.lang.Exception
import java.util.*

class VocaViewModel(application: Application) : AndroidViewModel(application){
    // file path of text file
    private val filePath: String = "word.txt"
    private val TAG = "VocaViewModel"

    // words list [ eng : kor(meaning) = 1 : n ]
    val words: MutableList<Voca> = mutableListOf<Voca>()

    init {
        try {
            val scan2 = Scanner(application.openFileInput("out.txt"))
            readFile(scan2)
        }catch (e: Exception) {
            Log.e(TAG, "사용자가 추가한 단어 없음")
        }
        val scan1 = Scanner(application.resources.openRawResource(R.raw.word))
        readFile(scan1)
    }
    fun readFile(scan: Scanner) {
        while(scan.hasNext()){
            val input = scan.nextLine()
            val splitInput = input.split(":")
            val meanings = splitInput[1].split("/")
            words.add(Voca(splitInput[0], meanings.toMutableList()))
        }
        scan.close()
    }

}