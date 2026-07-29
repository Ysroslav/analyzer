package ru.bodrov.analyzer.service

import ru.bodrov.analyzer.model.Candle
import ru.bodrov.analyzer.model.Tick
import java.time.LocalDate
import kotlin.time.Duration

interface TickService {

    // возвращает отфильтрованный список, не создает новый список
    fun filterByTicker(ticks: List<Tick>, ticker: String): List<Tick>

    // возвращает отфильтрованный список, не создает новый список
    fun filterByPeriod(ticks: List<Tick>, period: Int): List<Tick>

    // возвращает отсортированный список, создает новый список
    fun sortByDateTime(ticks: List<Tick>): List<Tick>

    // возвращает отфильтрованный список, не создает новый список
    fun filterByDate(ticks: List<Tick>, from: LocalDate, to: LocalDate): List<Tick>

    // агрегирует тики в свечи, всегда возвращает новый список
    fun aggregateToCandles(ticks: List<Tick>, timeframe: Duration): List<Candle>

}