package com.mp.vocabulary.server.data

class TranslationResponse (
    var srcLangType: String,
    var tarLangType: String,
    var translatedText: String
    )

enum class CODE {
    ko, en
}