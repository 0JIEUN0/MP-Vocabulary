package com.mp.vocabulary.viewmodel

import android.app.Application
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.database.*

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "UserViewModel"

    // user
    val userPhoneId: String = Settings.Secure.getString(application.contentResolver, Settings.Secure.ANDROID_ID) // android device id
    var userId: String? = null

    // Firebase Realtime Database
    val userDBRefetence: DatabaseReference

    init {
        // user 단말기 정보
        userDBRefetence = FirebaseDatabase.getInstance().getReference("User")
        searchUser()

        Log.d("this--->", userPhoneId)

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