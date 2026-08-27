package ru.bodrov.analyzer

import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.core.io.ClassPathResource
import ru.bodrov.analyzer.common.Timeframe
import ru.bodrov.analyzer.csv.CsvReader
import ru.bodrov.analyzer.service.TickService
import ru.bodrov.analyzer.source.CsvTickSource
import java.io.ByteArrayInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.Test
import kotlin.test.assertTrue

class TickServiceAggregateTest {

    val resource = ClassPathResource("csv/valid.csv")
    val csvReader = CsvReader()
    val source = CsvTickSource(csvReader, resource)
    val tradeStart = "10.00.00"
    val tickService: TickService = TickService(source, tradeStart)

    @Test
    fun `M1 aggregate ticks to candles`() {
        val csv = """
            <TICKER>;<PER>;<DATE>;<TIME>;<LAST>;<VOL>
            GC;0;20260716;100000;4041.7;1
            GC;0;20260716;100059;4041.5;1
            GC;0;20260716;100101;4041.8;1
            GC;0;20260716;100159;4041.11;1
            GC;0;20260716;100200;4041.17;1
        """.trimIndent()

        val stream = ByteArrayInputStream(csv.toByteArray())

        val ticks = csvReader.readTicks(stream)
        val candles = tickService.aggregateToCandles("GC", ticks, Timeframe.M1)
        assertEquals(3, candles.size)
        assertEquals(4041.5, candles[0].close)
        assertEquals(4041.7, candles[0].high)
        assertEquals(4041.7, candles[0].open)
        assertEquals(4041.5, candles[0].low)
        assertEquals(2, candles[0].volume)
        assertEquals(4041.8, candles[1].open)
        assertEquals(4041.11, candles[1].close)
        assertEquals(4041.17, candles[2].close)
    }

    @Test
    fun `M5 aggregate ticks to candles`() {
        val csv = """
            <TICKER>;<PER>;<DATE>;<TIME>;<LAST>;<VOL>
            GC;0;20260716;100000;4041.7;1
            GC;0;20260716;100459;4041.5;1
            GC;0;20260716;100500;4041.8;1
            GC;0;20260717;100959;4041.11;1
            GC;0;20260717;100000;4041.17;1
        """.trimIndent()

        val stream = ByteArrayInputStream(csv.toByteArray())

        val ticks = csvReader.readTicks(stream)
        val candles = tickService.aggregateToCandles("GC", ticks, Timeframe.M5)
        assertEquals(4, candles.size)
        assertEquals(4041.5, candles[0].close)
        assertEquals(4041.7, candles[0].open)
        assertEquals(4041.17, candles[2].close)
        assertEquals(4041.11, candles[3].close)
    }

    @Test
    fun `M15 aggregate ticks to candles`() {
        val csv = """
            <TICKER>;<PER>;<DATE>;<TIME>;<LAST>;<VOL>
            GC;0;20260716;100000;4041.7;1
            GC;0;20260716;100459;4041.5;1
            GC;0;20260716;100500;4041.8;1
            GC;0;20260716;120959;4041.11;1
            GC;0;20260716;150030;4041.17;1
        """.trimIndent()

        val stream = ByteArrayInputStream(csv.toByteArray())

        val ticks = csvReader.readTicks(stream)
        val candles = tickService.aggregateToCandles("GC", ticks, Timeframe.M15)
        assertEquals(3, candles.size)
        assertEquals(4041.8, candles[0].close)
        assertEquals(4041.8, candles[0].high)
        assertEquals(4041.11, candles[1].close)
        assertEquals(4041.17, candles[2].close)
    }

    @Test
    fun `H1 aggregate ticks to candles`() {
        val csv = """
            <TICKER>;<PER>;<DATE>;<TIME>;<LAST>;<VOL>
            GC;0;20260716;100000;4041.7;1
            GC;0;20260716;100459;4041.5;1
            GC;0;20260716;110000;4041.8;1
            GC;0;20260716;120000;4041.11;1
            GC;0;20260716;150000;4041.17;1
        """.trimIndent()

        val stream = ByteArrayInputStream(csv.toByteArray())

        val ticks = csvReader.readTicks(stream)
        val candles = tickService.aggregateToCandles("GC", ticks, Timeframe.H1)
        assertEquals(4, candles.size)
        assertEquals(4041.5, candles[0].close)
        assertEquals(4041.7, candles[0].high)
        assertEquals(4041.8, candles[1].close)
        assertEquals(4041.11, candles[2].close)
    }

    @Test
    fun `D1 aggregate ticks to candles`() {
        val csv = """
            <TICKER>;<PER>;<DATE>;<TIME>;<LAST>;<VOL>
            GC;0;20260716;100000;4041.7;1
            GC;0;20260716;100459;4041.5;1
            GC;0;20260716;110000;4041.8;1
            GC;0;20260716;120000;4041.11;1
            GC;0;20260718;150000;4041.17;1
        """.trimIndent()

        val stream = ByteArrayInputStream(csv.toByteArray())

        val ticks = csvReader.readTicks(stream)
        val candles = tickService.aggregateToCandles("GC", ticks, Timeframe.D1)
        assertEquals(2, candles.size)
        assertEquals(4041.11, candles[0].close)
        assertEquals(4041.8, candles[0].high)
        assertEquals(4041.17, candles[1].close)
    }

    @Test
    fun `W1 aggregate ticks to candles`() {
        val csv = """
            <TICKER>;<PER>;<DATE>;<TIME>;<LAST>;<VOL>
            GC;0;20260716;100000;4041.7;1
            GC;0;20260716;100459;4041.5;1
            GC;0;20260716;110000;4041.8;1
            GC;0;20260716;120000;4041.11;1
            GC;0;20260723;150000;4041.17;1
        """.trimIndent()

        val stream = ByteArrayInputStream(csv.toByteArray())

        val ticks = csvReader.readTicks(stream)
        val candles = tickService.aggregateToCandles("GC", ticks, Timeframe.W1)
        assertEquals(2, candles.size)
        assertEquals(4041.11, candles[0].close)
        assertEquals(4041.8, candles[0].high)
        assertEquals(4041.17, candles[1].close)
    }

    @Test
    fun `MN1 aggregate ticks to candles`() {
        val csv = """
            <TICKER>;<PER>;<DATE>;<TIME>;<LAST>;<VOL>
            GC;0;20260716;100000;4041.7;1
            GC;0;20260716;100459;4041.5;1
            GC;0;20260716;110000;4041.8;1
            GC;0;20260816;090000;4041.11;1
            GC;0;20260816;100000;4041.17;1
        """.trimIndent()

        val stream = ByteArrayInputStream(csv.toByteArray())

        val ticks = csvReader.readTicks(stream)
        val candles = tickService.aggregateToCandles("GC", ticks, Timeframe.MN1)
        assertEquals(2, candles.size)
        assertEquals(4041.11, candles[0].close)
        assertEquals(4041.8, candles[0].high)
        assertEquals(4041.17, candles[1].close)
    }

    @Test
    fun `aggregate empty list`() {
        val candles = tickService.aggregateToCandles("GC", emptyList(), Timeframe.M5)
        assertTrue { candles.isEmpty() }
    }
}
