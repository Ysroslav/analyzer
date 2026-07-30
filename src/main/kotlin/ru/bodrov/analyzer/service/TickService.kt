package ru.bodrov.analyzer.service

import org.springframework.stereotype.Service
import ru.bodrov.analyzer.common.Timeframe
import ru.bodrov.analyzer.model.Candle
import ru.bodrov.analyzer.model.Tick
import ru.bodrov.analyzer.source.TickSource
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.time.Duration

@Service
class TickService(
    private val tickSource: TickSource) {

    fun loadTicks(): List<Tick> = tickSource.load()

    fun filterByTicker(
        ticks: List<Tick>,
        ticker: String
    ): List<Tick> = ticks.filter { it.ticker == ticker }

    fun filterByPeriod(
        ticks: List<Tick>,
        period: Int
    ): List<Tick> = ticks.filter { it.period == period }

    fun sortByDateTime(ticks: List<Tick>): List<Tick> =
        ticks.sortedBy { it.dateTime }

    fun filterByDate(
        ticks: List<Tick>,
        from: LocalDateTime,
        to: LocalDateTime
    ): List<Tick> = ticks.filter { it.dateTime in from..to }

    // агрегирует тики в свечи, всегда возвращает новый список
    fun aggregateToCandles(
        ticks: List<Tick>,
        timeframe: Timeframe
    ): List<Candle> {
        TODO("Not yet implemented")
    }
}