package ru.bodrov.analyzer.csv

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.springframework.stereotype.Component
import ru.bodrov.analyzer.model.Tick
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Component
class CsvReader {

    private companion object {
        private const val TICKER = "<TICKER>"
        private const val PERIOD = "<PER>"
        private const val DATE = "<DATE>"
        private const val TIME = "<TIME>"
        private const val PRICE = "<LAST>"
        private const val VOLUME = "<VOL>"
    }

    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")

    fun readTicks(inputStream: InputStream): List<Tick> = csvReader {
        delimiter = ';'
    }.open(inputStream) {

        readAllWithHeaderAsSequence()
            .map {
            Tick (
                ticker = it[TICKER]?.trim() ?: error("Missing ticker value"),
                period = it[PERIOD]?.trim()?.toIntOrNull() ?: error("Invalid period value"),
                dateTime = parseStringToDateTime(it[DATE]?.trim() + it[TIME]?.trim()),
                price = it[PRICE]?.trim()?.toDoubleOrNull() ?: error("Invalid price value"),
                volume = it[VOLUME]?.trim()?.toLongOrNull() ?: error("Invalid volume value")
            )
        }.toList()
    }

    private fun parseStringToDateTime(str: String): LocalDateTime {
        try {
            return LocalDateTime.parse(str, formatter)
        } catch (e: DateTimeParseException) {
            error("Cannot parse text: '$str'")
        }
    }
}