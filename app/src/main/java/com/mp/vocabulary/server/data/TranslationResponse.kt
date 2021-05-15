package com.mp.vocabulary.server.data

class TranslationResponse (var message: TranslationResultResponse)
class TranslationResultResponse(var result: TranslationResult)

class TranslationResult (
        var srcLangType: String,
        var tarLangType: String,
        var translatedText: String
        )

enum class CODE {
    ko, en
}