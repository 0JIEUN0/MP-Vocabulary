package com.mp.vocabulary.data

data class QuizVoca(var eng: String, var kor: String, val others: MutableList<String>, var isCorrect: Boolean = true)