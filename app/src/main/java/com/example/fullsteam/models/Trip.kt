package com.example.fullsteam.models

import com.google.type.DateTime

data class Trip(
    val dateTime: DateTime,
    val trainBrand: TrainBrand,
    val trainNumber: Number,
    val trainName: String,
    val carrier: Carrier,
    val startStation: String,
    val endStation: String,
    val kmDistance: Number,
    val tripTimeInMinutes: Number,
    val price: Double,
    val pricePerKm: Double,
    val avgSpeed: Double,
    val hasChange: Boolean,
    val bike: Boolean,
    val delay: Number,
    val comment: String
)