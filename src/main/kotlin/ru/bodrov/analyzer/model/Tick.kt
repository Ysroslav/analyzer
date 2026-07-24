package ru.bodrov.analyzer.model

data class Tick (
    val ticker: String,
    val period: Int,
    val date: String,
    val time: String,
    val price: Double,
    val volume: Long
)