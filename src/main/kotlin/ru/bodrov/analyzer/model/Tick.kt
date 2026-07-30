package ru.bodrov.analyzer.model

import java.time.LocalDateTime

data class Tick (
    val ticker: String,
    val period: Int,
    val dateTime: LocalDateTime,
    val price: Double,
    val volume: Long
)