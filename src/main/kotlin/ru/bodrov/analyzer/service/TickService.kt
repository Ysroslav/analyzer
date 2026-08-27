package ru.bodrov.analyzer.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import ru.bodrov.analyzer.common.Timeframe
import ru.bodrov.analyzer.model.Candle
import ru.bodrov.analyzer.model.Tick
import ru.bodrov.analyzer.source.TickSource
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration

@Service
class TickService(
    private val tickSource: TickSource,
    @Value("\${trade.start}") private val tradeStart: String
) {

    private val formatterTime: DateTimeFormatter = DateTimeFormatter.ofPattern("HH.mm.ss")

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
        ticker: String,
        ticks: List<Tick>,
        timeframe: Timeframe
    ): List<Candle> {
        val filteredTicks = ticks
            .filter { it.ticker == ticker }
            .sortedBy { it.dateTime }

        if (filteredTicks.isEmpty()) return emptyList()

        val startTrade = getStartTimeTradeForFirstTick(filteredTicks.first())

        val tradeTicks = filteredTicks
            .filter { it.dateTime >= startTrade }

        if (tradeTicks.isEmpty()) return emptyList()

        return tradeTicks
            .groupBy { tick ->
                timeframe.calculateStartTime(
                    tick.dateTime,
                    startTrade
                )
            }
            .toSortedMap()
            .map { (startTime, candleTicks) ->
                createCandle(
                    startTime,
                    candleTicks,
                    timeframe
                )
            }
    }

    private fun createCandle(
        startTime: LocalDateTime,
        ticks: List<Tick>,
        timeframe: Timeframe
    ): Candle {
        return Candle(
            timeframe = timeframe,
            dateTimeOpen = startTime,
            open = ticks.first().price,
            high = ticks.maxOf { it.price },
            low = ticks.minOf { it.price },
            close = ticks.last().price,
            volume = ticks.sumOf { it.volume }
        )
    }

    fun getStartTimeTradeForFirstTick(tick: Tick) : LocalDateTime {
        val startTrade = LocalTime.parse(tradeStart, formatterTime)

        return LocalDateTime.of(
            tick.dateTime.year,
            tick.dateTime.monthValue,
            tick.dateTime.dayOfMonth,
            startTrade.hour,
            startTrade.minute,
            startTrade.second)
    }
}