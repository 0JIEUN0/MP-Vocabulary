package com.mp.vocabulary.viewmodel

import android.app.Application
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.mp.vocabulary.adapter.UserStudyAdapter
import com.mp.vocabulary.data.UserData
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "UserViewModel"
    val scope = CoroutineScope(Dispatchers.Main)

    // user
    val userPhoneId: String = Settings.Secure.getString(application.contentResolver, Settings.Secure.ANDROID_ID) // android device id
    var userId: String? = null
    var count: Long? = null

    // Firebase Realtime Database
    val userDBRefetence: DatabaseReference

    init {
        userDBRefetence = FirebaseDatabase.getInstance().getReference("User")
        searchUser()
    }

    fun addOptionToAdapter(limit: Int): UserStudyAdapter {
        val rdb : DatabaseReference = FirebaseDatabase.getInstance().getReference("User/$userPhoneId/date")
        val query = rdb.limitToLast(limit)
        val option = FirebaseRecyclerOptions.Builder<UserData>()
                .setQuery(query, UserData::class.java)
                .build()
        return UserStudyAdapter(option)
    }

    fun addStudyingAmount(number: Int) {
        val localDateTime: LocalDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ISO_DATE
        val formatted = localDateTime.format(formatter)

        val data: UserData = UserData(formatted, number)
        userDBRefetence.child(userPhoneId).child("date").child(formatted).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        count = snapshot.child("num").value as Long?
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.w(TAG, "loadPost:onCancelled", error.toException())
                    }
                }
        )
        if(count == null) {
            // 새로운 날짜
            userDBRefetence.child(userPhoneId).child("date").child(formatted).setValue(data)
        } else {
            data.num += count!!.toInt()
            Log.d("This-->", data.num.toString())
            userDBRefetence.child(userPhoneId).child("date").child(formatted).setValue(data)
        }
    }


    fun initUser(userId: String){
        // 사용자 닉네임 등록
        this.userId = userId
        userDBRefetence.child(userPhoneId).child("id").setValue(userId)
    }

    private fun searchUser(){
        // 해당 기기의 ID 로 닉네임을 등록한 적 있는지 확인
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
}