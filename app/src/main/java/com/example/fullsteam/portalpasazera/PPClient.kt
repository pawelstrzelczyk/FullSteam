package com.example.fullsteam.portalpasazera

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.jackson.responseObject
import com.github.kittinunf.result.onError
import com.github.kittinunf.result.success
import java.util.concurrent.Executors

class PPClient {
    private val mapper =
        jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
    private val baseURL: String =
        "https://portalpasazera.pl/Wyszukiwarka/WyszukajStacje"
    private val executor = Executors.newSingleThreadExecutor()

    fun getStations(station: String): MutableLiveData<MutableList<List<Station>>> {
        val ppStations: MutableLiveData<MutableList<List<Station>>> = MutableLiveData()
        executor.execute {
            val ppStationsExecutor: MutableList<List<Station>> = arrayListOf()
            station.let { station ->
                val response = baseURL.httpGet(
                    listOf(
                        Pair("wprowadzonyTekst", station)
                    )
                ).responseObject<List<Station>>(mapper).third
                Log.d("response", response.toString())
                response.onError {
                    Log.e("response failed", ":((((((((((((((((")
                }
                response.success {
                    ppStationsExecutor.add(it)
                }
            }
            ppStations.postValue(ppStationsExecutor)
        }

        return ppStations
    }

}