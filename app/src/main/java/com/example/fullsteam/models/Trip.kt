package com.example.fullsteam.models

import com.google.firebase.firestore.PropertyName


data class Trip(
    @get: PropertyName("dateTime")
    @set: PropertyName("dateTime")
    var dateTime: String="",
    @get: PropertyName("trainBrand")
    @set: PropertyName("trainBrand")//DateTime,
    var trainBrand: String="",
    @get: PropertyName("trainNumber")
    @set: PropertyName("trainNumber")//TrainBrand,
    var trainNumber: Int=0,
    @get: PropertyName("trainName")
    @set: PropertyName("trainName")
    var trainName: String="",
    @get: PropertyName("carrier")
    @set: PropertyName("carrier")
    var carrier: String="",//Carrier,
    @get: PropertyName("startStation")
    @set: PropertyName("startStation")
    var startStation: String="",
    @get: PropertyName("endStation")
    @set: PropertyName("endStation")
    var endStation: String="",
    @get: PropertyName("kmDistance")
    @set: PropertyName("kmDistance")
    var kmDistance: Int=0,
    @get: PropertyName("tripTimeInMinutes")
    @set: PropertyName("tripTimeInMinutes")
    var tripTimeInMinutes: Int=0,
    @get: PropertyName("price")
    @set: PropertyName("price")
    var price: Double=0.0,
    @get: PropertyName("pricePerKm")
    @set: PropertyName("pricePerKm")
    var pricePerKm: Double=0.0,
    @get: PropertyName("avgSpeed")
    @set: PropertyName("avgSpeed")
    var avgSpeed: Double=0.0,
    @get: PropertyName("hasChange")
    @set: PropertyName("hasChange")
    var hasChange: Boolean=false,
    @get: PropertyName("hasBike")
    @set: PropertyName("hasBike")
    var hasBike: Boolean=false,
    @get: PropertyName("isPKM")
    @set: PropertyName("isPKM")
    var isPKM: Boolean=false,
    @get: PropertyName("isSleepingCar")
    @set: PropertyName("isSleepingCar")
    var isSleepingCar: Boolean=false,
    @get: PropertyName("delay")
    @set: PropertyName("delay")
    var delay: Int=0,
    @get: PropertyName("comment")
    @set: PropertyName("comment")
    var comment: String=""

) {
//    constructor() : this(
//        "", "", 0, "", "", "", "",
//        0, 0, 0.0, 0.0, 0.0, false,
//        false, false, false, 0, ""
//    )

    fun tripToJson(): MutableMap<String, Any> {
        val json: MutableMap<String, Any> = HashMap()
        json["dateTime"] = dateTime
        json["trainBrand"] = trainBrand
        json["trainNumber"] = trainNumber
        json["trainName"] = trainName
        json["carrier"] = carrier
        json["startStation"] = startStation
        json["endStation"] = endStation
        json["kmDistance"] = kmDistance
        json["tripTimeInMinutes"] = tripTimeInMinutes
        json["price"] = price
        json["pricePerKm"] = pricePerKm
        json["avgSpeed"] = avgSpeed
        json["hasChange"] = hasChange
        json["hasBike"] = hasBike
        json["isPKM"] = isPKM
        json["isSleepingCar"] = isSleepingCar
        json["delay"] = delay
        json["comment"] = comment
        return json
    }
}