package ru.bodrov.analyzer

import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.core.io.ClassPathResource
import ru.bodrov.analyzer.csv.CsvReader
import ru.bodrov.analyzer.source.CsvTickSource
import kotlin.test.Test
import kotlin.test.assertTrue

class CsvTickSourceTest {

    @Test
    fun shouldLoadCsv() {
        val resource = ClassPathResource("source/GC_260716_260716.csv")
        val csvReader = CsvReader()
        val source = CsvTickSource(csvReader, resource)

        val ticks = source.load()
        assertTrue(ticks.isNotEmpty())
        assertEquals("GC", ticks.first().ticker)
        assertEquals("GC", ticks.last().ticker)
    }
}