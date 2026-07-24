package ru.bodrov.analyzer

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import ru.bodrov.analyzer.csv.CsvReader
import java.io.ByteArrayInputStream
import kotlin.test.assertTrue

class CsvReaderTest {

    private var csvReader: CsvReader = CsvReader()

    @Test
    fun shouldReadValidCsv() {
        val stream = ClassPathResource("csv/valid.csv").inputStream

        val ticks = csvReader.readTicks(stream)

        assertEquals(2, ticks.size)
        assertEquals("GC", ticks.first().ticker)
        assertEquals("4041.8".toDouble(), ticks.first().price)
        assertEquals("4041.5".toDouble(), ticks.last().price)
    }

    @Test
    fun shouldReadRealMarketFile() {
        val stream = ClassPathResource("GC_260716_260716.csv").inputStream

        val ticks = csvReader.readTicks(stream)

        assertTrue(ticks.isNotEmpty())
        assertEquals("GC", ticks.first().ticker)
        assertEquals("GC", ticks.last().ticker)
    }

    @Test
    fun shouldReturnEmptyListForEmptyFile() {
        val csv = """
        """.trimIndent()
        val stream = ByteArrayInputStream(csv.toByteArray())

        val ticks = csvReader.readTicks(stream)

        assertTrue(ticks.isEmpty())
    }

    @Test
    fun shouldFailWhenPriceIsInvalid() {

        val csv = """
            <TICKER>;<PER>;<DATE>;<TIME>;<LAST>;<VOL>
            GC;0;20260716;000000;4041.8.7;1
            GC;0;20260716;000000;4041.5;1
        """.trimIndent()
        val stream = ByteArrayInputStream(csv.toByteArray())

        assertThatThrownBy { csvReader.readTicks(stream) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("Invalid price value")
    }

    @Test
    fun shouldFailWhenRequiredColumnIsMissing() {
        val csv = """
            <PER>;<DATE>;<TIME>;<LAST>;<VOL>
            0;20260716;000000;4041.8;1
            0;20260716;000000;4041.5;1
        """.trimIndent()
        val stream = ByteArrayInputStream(csv.toByteArray())

        assertThatThrownBy { csvReader.readTicks(stream) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("Missing ticker value")
    }

    @Test
    fun shouldTrimValues () {
        val csv = """
            <TICKER>;<PER>;<DATE>;<TIME>;<LAST>;<VOL>
            GC    ;0;20260716;000000;4041.8;1
            GC;0;20260716;000000  ;4041.5   ;1
        """.trimIndent()
        val stream = ByteArrayInputStream(csv.toByteArray())
        val ticks = csvReader.readTicks(stream)

        assertEquals(2, ticks.size)
        assertEquals("GC", ticks.first().ticker)
        assertEquals("4041.8".toDouble(), ticks.first().price)
        assertEquals("4041.5".toDouble(), ticks.last().price)
    }


}