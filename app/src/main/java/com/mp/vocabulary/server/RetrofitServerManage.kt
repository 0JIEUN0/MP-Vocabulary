package com.mp.vocabulary.server

import android.util.Log
import com.mp.vocabulary.server.data.TranslationRequest
import com.mp.vocabulary.server.data.TranslationResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

object RetrofitServerManage {
    private val TAG = "Server"
    var retrofit: Retrofit? = null
    var apiInterface: ApiInterface? = null

    fun initRetrofit(): Boolean {
        retrofit = try {
            Retrofit.Builder()
                .baseUrl("https://openapi.naver.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        } catch (e: Exception) {
            Log.e(TAG, "Server init failed!!")
            Log.e(TAG, e.stackTraceToString())
            null
        }

        apiInterface = retrofit?.create(ApiInterface::class.java) ?: null

        return retrofit != null
    }

    fun translation(headers: HashMap<String,Any>,
                    source: String,
                    target: String,
                    text: String
    ): String {
        val transFunc: Call<TranslationResponse>? = apiInterface?.papagoTranslation(
            headerMap = headers,
            request = TranslationRequest(
                    source = source,
                    target = target,
                    text = text
            )
        )

        val response: Response<TranslationResponse>? = transFunc?.execute()

        if(response!!.isSuccessful) {
            return response.body()!!.message.result.translatedText
        }
        else {
            Log.e(TAG, response.errorBody()!!.string())
        }
        return ""
    }
}