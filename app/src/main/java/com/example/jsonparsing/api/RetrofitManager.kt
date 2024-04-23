package com.example.jsonparsing.api

import android.content.Context
import com.example.jsonparsing.MainActivity
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitManager {
    companion object {
        val instance = RetrofitManager()
    }

    private val request = RetrofitClient.instance().create(RetrofitInterface::class.java)

    private lateinit var mainActivity: MainActivity
    private lateinit var context: Context

    fun request(
        context: Context,
        completion: (Pair<Int, String>) -> Unit
    ) {

        this.mainActivity = context as MainActivity
        this.context = context

        request.methodGET("")
            .enqueue(callback(completion))
    }

    private fun callback(completion: (Pair<Int, String>) -> Unit): Callback<JsonElement> {
        return object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.code() == 200) {
                    val data = Gson().fromJson(response.body().toString(), JsonObject::class.java)
                    completion(Pair(response.code(), data.toString()))
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                completion(Pair(-1, t.message.toString()))
            }
        }
    }
}

enum class Method {
    GET,
    POST,
}