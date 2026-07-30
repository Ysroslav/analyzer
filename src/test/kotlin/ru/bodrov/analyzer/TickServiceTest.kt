package ru.bodrov.analyzer

import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.core.io.ClassPathResource
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
    val tickService: TickService = TickService(source)

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
}