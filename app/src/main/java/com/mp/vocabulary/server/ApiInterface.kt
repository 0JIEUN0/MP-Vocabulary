package com.mp.vocabulary.server

import com.mp.vocabulary.server.data.TranslationResponse
import retrofit2.Call
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {
    @POST("https://openapi.naver.com/v1/papago/n2mt")
    fun papagoTranslation(@HeaderMap headerMap: HashMap<String, Any>,
                          @Query("source") sourceId: String,
                          @Query("target") targetId: String,
                          @Query("text") text: String
    ): Call<TranslationResponse>
}