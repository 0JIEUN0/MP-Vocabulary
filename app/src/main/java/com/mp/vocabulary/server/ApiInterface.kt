package com.mp.vocabulary.server

import com.mp.vocabulary.server.data.TranslationRequest
import com.mp.vocabulary.server.data.TranslationResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @POST("https://openapi.naver.com/v1/papago/n2mt")
    fun papagoTranslation(@HeaderMap headerMap: HashMap<String, Any>,
                          @Body request: TranslationRequest
    ): Call<TranslationResponse>
}