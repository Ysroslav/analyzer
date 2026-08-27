package ru.bodrov.analyzer.model

import ru.bodrov.analyzer.common.Timeframe
import java.time.LocalDateTime

class Candle(
    val timeframe: Timeframe,
    val dateTimeOpen: LocalDateTime,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Long,
)