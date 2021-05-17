package com.mp.vocabulary.data

import java.util.*

data class User(var userName: String, val data: MutableList<UserData>)

data class UserData(var date: Date, var num: Int)