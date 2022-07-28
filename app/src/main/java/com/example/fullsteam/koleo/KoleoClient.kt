package com.example.fullsteam.koleo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.fullsteam.koleo.brands.BrandsResponse
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.jackson.responseObject
import com.github.kittinunf.result.onError
import com.github.kittinunf.result.success
import java.util.concurrent.Executors

class KoleoClient {
    private val mapper =
        jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
    private val baseURL: String =
        "https://koleo.pl/pl/brands"
    private val executor = Executors.newSingleThreadExecutor()

    fun getBrands(): MutableLiveData<MutableList<BrandsResponse>> {
        val brands: MutableLiveData<MutableList<BrandsResponse>> = MutableLiveData()
        executor.execute {
            val ppStationsExecutor: MutableList<BrandsResponse> = arrayListOf()
            val response = baseURL.httpGet().responseObject<BrandsResponse>(mapper).third
            Log.d("response", response.toString())
            response.onError {
                Log.e("response failed", ":((((((((((((((((")
            }
            response.success {
                ppStationsExecutor.add(it)
            }
            brands.postValue(ppStationsExecutor)
        }

        return brands
    }
}