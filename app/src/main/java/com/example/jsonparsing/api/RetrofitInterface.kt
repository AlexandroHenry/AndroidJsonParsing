package com.example.jsonparsing.api

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface RetrofitInterface {

    @POST
    fun methodPOST(
        @Url url: String,
        @Body jsonObject: JsonObject,
    ): Call<JsonElement>

    @GET
    fun methodGET(
        @Url url: String,
    ): Call<JsonElement>

}