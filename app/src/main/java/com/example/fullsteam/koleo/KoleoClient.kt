package com.example.fullsteam.koleo

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.fullsteam.koleo.brands.BrandsResponse
import com.example.fullsteam.koleo.carriers.CarriersResponse
import com.example.fullsteam.koleo.train_calendars.TrainCalendarsResponse
import com.example.fullsteam.koleo.trains.TrainResponse
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.jackson.responseObject
import com.github.kittinunf.result.onError
import com.github.kittinunf.result.success
import java.util.concurrent.Executors

class KoleoClient {
    private val mapper =
        jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)

    private val executor = Executors.newSingleThreadExecutor()

    private fun initClient() {
        FuelManager.instance.basePath = "https://koleo.pl/pl"
    }

    init {
        initClient()
    }

    fun getBrands(): MutableLiveData<MutableList<BrandsResponse>> {
        val brands: MutableLiveData<MutableList<BrandsResponse>> = MutableLiveData()
        executor.execute {
            val ppStationsExecutor: MutableList<BrandsResponse> = arrayListOf()
            val response =
                "/brands".httpGet().responseObject<BrandsResponse>(mapper).third
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

    fun getCarriers(): MutableLiveData<MutableList<CarriersResponse>> {
        val carriers: MutableLiveData<MutableList<CarriersResponse>> = MutableLiveData()
        executor.execute {
            val ppStationsExecutor: MutableList<CarriersResponse> = arrayListOf()
            val response =
                "/carriers".httpGet().responseObject<CarriersResponse>(mapper).third
            Log.d("response", response.toString())
            response.onError {
                Log.e("response failed", ":((((((((((((((((")
            }
            response.success {
                ppStationsExecutor.add(it)
            }
            carriers.postValue(ppStationsExecutor)
        }

        return carriers
    }

    fun getTrainCalendars(
        context: Context,
        brand: String,
        nr: Number,
        name: String
    ): MutableLiveData<MutableList<TrainCalendarsResponse>> {
        val trainCalendars: MutableLiveData<MutableList<TrainCalendarsResponse>> =
            MutableLiveData()
        executor.execute {
            val ppStationsExecutor: MutableList<TrainCalendarsResponse> = arrayListOf()
            val response =
                "/train_calendars".httpGet(
                    listOf(
                        Pair("brand", brand),
                        Pair("nr", nr),
                        Pair("name", name)
                    )
                ).responseObject<TrainCalendarsResponse>(mapper).third

            response.onError {
                try {
                    Log.e("response failed", ":((((((((((((((((")
                } catch (e: FuelError) {

                }

            }
            response.success {
                ppStationsExecutor.add(it)
            }
            trainCalendars.postValue(ppStationsExecutor)
        }

        return trainCalendars
    }

    fun getTrain(context: Context, id: Int): MutableLiveData<MutableList<TrainResponse>> {
        val train: MutableLiveData<MutableList<TrainResponse>> =
            MutableLiveData()
        executor.execute {
            val ppStationsExecutor: MutableList<TrainResponse> = arrayListOf()

            val response =
                "/trains/$id".httpGet().responseObject<TrainResponse>(mapper).third
            Log.d("response", response.get().train.name)
            response.onError {
                try {
                    Log.e("response failed", ":((((((((((((((((")
                } catch (e: FuelError) {

                }

            }
            response.success {
                ppStationsExecutor.add(it)
            }
            train.postValue(ppStationsExecutor)
        }
        return train
    }
}