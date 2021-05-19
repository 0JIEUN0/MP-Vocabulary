package com.mp.vocabulary.viewmodel

import android.app.Application
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "UserViewModel"

    // user
    val userPhoneId: String = Settings.Secure.getString(application.contentResolver, Settings.Secure.ANDROID_ID) // android device id
    var userId: String? = null
    var count: Long? = null

    // Firebase Realtime Database
    val userDBRefetence: DatabaseReference
    val userDBRefetence2: DatabaseReference

    init {
        // user 단말기 정보
        userDBRefetence = FirebaseDatabase.getInstance().getReference("User")
        userDBRefetence2 = FirebaseDatabase.getInstance().getReference("User")
        searchUser()
    }

    fun addStudyingAmount(number: Int) {
        val localDateTime: LocalDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ISO_DATE
        val formatted = localDateTime.format(formatter)

        Log.d("This-->", userDBRefetence.child(userPhoneId).child(formatted).key.toString())
        val dateListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                count = dataSnapshot.child(userPhoneId).child(formatted).value as Long?
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        userDBRefetence.addValueEventListener(dateListener)

        if(count == null) {
            // 새로운 날짜
            userDBRefetence.child(userPhoneId).child(formatted).setValue(number)
        } else {
            userDBRefetence.child(userPhoneId).child(formatted).setValue(number.toLong() + count!!)
        }
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
}