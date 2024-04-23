package com.example.jsonparsing

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jsonparsing.api.RetrofitManager
import com.example.jsonparsing.app.LogPrint
import com.example.jsonparsing.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btn1.setOnClickListener(this@MainActivity)
        binding.btn2.setOnClickListener(this@MainActivity)
        binding.btn3.setOnClickListener(this@MainActivity)
    }

    fun requestAPI(
        context: Context,
        completion: (JsonElement) -> Unit
    ) {
        RetrofitManager.instance.request(context) {
            when (it.first) {
                200 -> {
                    completion(Gson().fromJson(it.second, JsonElement::class.java))
                }

                else -> {
                    LogPrint.d("${it.second}")
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btn1 -> {
                requestAPI(this@MainActivity) {
                    val results = JSONArray(JSONObject(it.toString()).get("results").toString())
                    val info = JSONObject(JSONObject(it.toString()).get("info").toString())

                    val name = JSONObject(JSONObject(results[0].toString()).get("name").toString())
                    val title = name.getString("title")
                    val first = name.getString("first")
                    val last = name.getString("last")

                    binding.name.text = "$title $first $last"

                    val picture = JSONObject(JSONObject(results[0].toString()).get("picture").toString())

                    setPhoto(picture.getString("large"))
                }
            }

            binding.btn2 -> {
                requestAPI(this@MainActivity) {
                    it.asJsonObject.getAsJsonArray("results").forEach {
                        val result = it.asJsonObject

                        val name = result.getAsJsonObject("name")
                        val title = name.get("title").asString
                        val first = name.get("first").asString
                        val last = name.get("last").asString

                        binding.name.text = "$title $first $last"

                        val picture = result.getAsJsonObject("picture")
                        setPhoto(picture.get("large").asString)
                    }
                }
            }

            binding.btn3 -> {
                requestAPI(this@MainActivity) {
                    Gson().fromJson(it.toString(), JsonObject::class.java).getAsJsonArray("results").forEach {
                        val result = it.asJsonObject

                        val name = result.getAsJsonObject("name")
                        val title = name.get("title").asString
                        val first = name.get("first").asString
                        val last = name.get("last").asString

                        binding.name.text = "$title $first $last"

                        val picture = result.getAsJsonObject("picture")
                        setPhoto(picture.get("large").asString)
                    }
                }
            }
        }
    }

    private fun setPhoto(url: String) {
        Glide.with(this)
            .load(url)
            .transform(CenterCrop(), RoundedCorners(12))
            .into(binding.profileImg)
    }
}