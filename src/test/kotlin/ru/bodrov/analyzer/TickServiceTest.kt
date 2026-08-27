package ru.bodrov.analyzer

import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.core.io.ClassPathResource
import ru.bodrov.analyzer.common.Timeframe
import ru.bodrov.analyzer.csv.CsvReader
import ru.bodrov.analyzer.service.TickService
import ru.bodrov.analyzer.source.CsvTickSource
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.Test
import kotlin.test.assertTrue

class TickServiceTest {

    val resource = ClassPathResource("csv/valid.csv")
    val csvReader = CsvReader()
    val source = CsvTickSource(csvReader, resource)
    val tradeStart = "10.00.00"
    val tickService: TickService = TickService(source, tradeStart)

    @Test
    fun filteredByTickerTest() {
        val ticks = tickService.loadTicks()
        val ticksFiltered = tickService.filterByTicker(ticks, "SBER")

        assertEquals(2, ticksFiltered.size)
        assertTrue(
            ticksFiltered.all {
                it.ticker == "SBER"
            }
        )
    }

    @Test
    fun filteredByPeriodTest() {
        val ticks = tickService.loadTicks()
        val ticksFiltered = tickService.filterByPeriod(ticks, 1)

        assertEquals(  1, ticksFiltered.size)
        assertTrue(
            ticksFiltered.all {
                it.period == 1
            }
        )
    }

    @Test
    fun sortByDateTimeTest() {
        val ticks = tickService.loadTicks()
        val ticksFiltered = tickService.sortByDateTime(ticks)

        assertEquals(  4, ticksFiltered.size)
        assertEquals("SBER", ticksFiltered.first().ticker)
    }

    @Test
    fun filterByDateTest() {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        val ticks = tickService.loadTicks()
        val ticksFiltered = tickService.filterByDate(
            ticks,
            LocalDateTime.parse("20260716020000", formatter),
            LocalDateTime.parse("20260716040000", formatter)
            )

        assertEquals(  2, ticksFiltered.size)
    }

    @Test
    fun aggregateToCandlesTest() {
        val stream = ClassPathResource("csv/check_aggregate.csv").inputStream

        val ticks = csvReader.readTicks(stream)

        val candles = tickService.aggregateToCandles("GC", ticks, Timeframe.M5)
        assertEquals(2, candles.size)
        assertEquals(4041.0, candles.iterator().next().open)
        assertEquals(4041.3, candles.iterator().next().close)
        assertEquals(4041.0, candles.iterator().next().low)
        assertEquals(4041.8, candles.iterator().next().high)
        assertEquals(5, candles.iterator().next().volume)
    }
}
