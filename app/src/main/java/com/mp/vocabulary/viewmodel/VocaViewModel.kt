package com.mp.vocabulary.viewmodel

import android.app.Application
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.mp.vocabulary.BuildConfig
import com.mp.vocabulary.R
import com.mp.vocabulary.data.QuizVoca
import com.mp.vocabulary.data.SimpleVoca
import com.mp.vocabulary.data.Voca
import com.mp.vocabulary.database.DBTable
import com.mp.vocabulary.database.VocaDBHelper
import com.mp.vocabulary.server.RetrofitServerManage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*

enum class FragmentRequest {
    REQUEST_QUIZ1, REQUEST_QUIZ2, REQUEST_NOTE, REQUEST_STAR
}

class VocaViewModel(application: Application) : AndroidViewModel(application){
    private val TAG = "VocaViewModel"

    val scope = CoroutineScope(Dispatchers.Main)

    // words list [ eng : kor(meaning) = 1 : n ]
    val words: MutableList<Voca> = mutableListOf<Voca>()
    val engs: MutableList<String> = mutableListOf<String>()

    // live data
    val searchResultWord: MutableLiveData<String> = MutableLiveData<String>()
    val searchResultMeaning: MutableLiveData<String> = MutableLiveData<String>()
    val isSearchFind: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val translatedString: MutableLiveData<String> = MutableLiveData<String>()

    val isQuizOk: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val wordListForQuiz: MutableLiveData<List<QuizVoca>> = MutableLiveData()

    // fragment translation
    val fragmentRequest: MutableLiveData<FragmentRequest> = MutableLiveData<FragmentRequest>()

    // SQLite [Database]
    val vocaDBHelper: VocaDBHelper // MainActivity 에서 값 할당
    var noteWordList: MutableList<Voca> = mutableListOf() // observer 가 인지할 수 있도록
    val noteWordListLiveData: MutableLiveData<MutableList<Voca>> = MutableLiveData() // 오답노트에 등록한 단어들
    var starWordList: MutableList<Voca> = mutableListOf() // observer 가 인지할 수 있도록
    val starWordListLiveData: MutableLiveData<MutableList<Voca>> = MutableLiveData() // 즐겨찾기한 단어들

    // user
    val userPhoneId: String
    var userId: String? = null

    // Firebase Realtime Database
    val userDBRefetence: DatabaseReference


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

        if(!RetrofitServerManage.initRetrofit()){
            Log.e(TAG, "Server init failed...")
        } else Log.e(TAG, "Server init success!")

        // init DB
        vocaDBHelper = VocaDBHelper(application.baseContext)
        noteWordList = vocaDBHelper.findAll(DBTable.NOTE)
        starWordList = vocaDBHelper.findAll(DBTable.STAR)
        noteWordListLiveData.value = noteWordList
        starWordListLiveData.value = starWordList

        // user 단말기 정보
        userPhoneId = Settings.Secure.getString(application.contentResolver, Settings.Secure.ANDROID_ID) // android device id
        //userId =
        userDBRefetence = FirebaseDatabase.getInstance().getReference("User")
        searchUser()


    }

    fun initUser(userId: String){
        this.userId = userId
        userDBRefetence.child(userPhoneId).child("id").setValue(userId)
    }

    private fun searchUser(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                userId = dataSnapshot.child(userPhoneId).child("id").value as String?
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        userDBRefetence.addValueEventListener(postListener)
    }

    fun deleteFromStar(data: Voca) {
        vocaDBHelper.deleteVoca(DBTable.STAR, data.eng)
        starWordList.remove(data)
        starWordListLiveData.value = starWordList
    }

    fun deleteFromNote(data: Voca) {
        vocaDBHelper.deleteVoca(DBTable.NOTE, data.eng)
        noteWordList.remove(data)
        noteWordListLiveData.value = noteWordList
    }

    fun insertToStar(data: Voca): Boolean{
        if(starWordList.contains(data)) return false
        val eng = data.eng
        data.kor.forEach {
            vocaDBHelper.insertVoca(DBTable.STAR, eng, it)
        }
        starWordList.add(data)
        starWordListLiveData.value = starWordList
        return true
    }

    fun insertToStar(eng: String): Boolean{
        val data: Voca = words.filter {
            it.eng == eng
        }[0]

        if(starWordList.contains(data)) return false
        val eng = data.eng
        data.kor.forEach {
            vocaDBHelper.insertVoca(DBTable.NOTE, eng, it)
        }
        starWordList.add(data)
        starWordListLiveData.value = starWordList
        return true
    }

    fun insertToNote(eng: String): Boolean {
        val data: Voca = words.filter {
            it.eng == eng
        }[0]

        if(noteWordList.contains(data)) return false
        val eng = data.eng
        data.kor.forEach {
            vocaDBHelper.insertVoca(DBTable.NOTE, eng, it)
        }
        noteWordList.add(data)
        noteWordListLiveData.value = noteWordList
        return true
    }

    fun makeQuiz(num: Int) {
        val random: Random = Random()
        val copyList = words.toMutableList()
        val quizWordList: MutableList<Voca> = mutableListOf<Voca>()

        for(i in 1 .. num){
            val idx = random.nextInt(copyList.size)
            quizWordList.add(copyList.removeAt(idx))
        }

        val quizList: MutableList<QuizVoca> = mutableListOf<QuizVoca>()
        quizWordList.forEach {
            val eng = it.eng
            val kor = it.kor[random.nextInt(it.kor.size)]
            val others = mutableListOf<String>()
            for(i in 1 .. 3){
                // 3개의 정답이 아닌 보기
                val str = copyList[random.nextInt(copyList.size)].kor[0]
                others.add(str)
            }
            quizList.add(
                QuizVoca(
                    eng = eng,
                    kor = kor,
                    others = others
            ))
        }
        wordListForQuiz.value = quizList.toMutableList()
        isQuizOk.value = true

    }

    fun fragmentTranslationRequest(target: FragmentRequest){
        fragmentRequest.value = target
    }

    fun search(target: String) {
        val result: List<Voca> = words.filter {
            it.eng == target
        }
        searchResultWord.value = target

        if(result.isEmpty()){
            searchResultMeaning.value = "검색 결과가 없습니다."
            isSearchFind.value = false
        }
        else {
            if(result.size != 1) {
                searchResultMeaning.value = "검색 결과가 없습니다."
                isSearchFind.value = false
                Log.e(TAG, "Warn: Searching English word must find EXACTLY one or 0!")
            }
            else {
                var resultMeanings = ""
                result[0].kor.forEach {
                    resultMeanings += "✔ $it \n"
                }
                searchResultMeaning.value = resultMeanings
                isSearchFind.value = true
            }
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


    fun translation(sourceCode: String, targetCode: String, str: String) {
        scope.launch {
            val headers: HashMap<String, Any> = HashMap()
            with(headers) {
                put("X-Naver-Client-Id", BuildConfig.PAPAGO_ID)
                put("X-Naver-Client-Secret", BuildConfig.PAPAGO_SECREAT)
            }
            var response: String =
                    withContext(Dispatchers.IO) {
                        RetrofitServerManage.translation(
                                headers = headers,
                                source = sourceCode,
                                target = targetCode,
                                text = str
                        )
                    }
            //binding!!.progressBar.visibility = View.GONE
            translatedString.value = response
        }
    }
}
