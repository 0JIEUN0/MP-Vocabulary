package com.mp.vocabulary.data

data class User(var userName: String, val data: MutableList<UserData>)

data class UserData(var date: String = "", var num: Int = 0)