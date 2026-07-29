package ru.bodrov.analyzer.service

import org.springframework.stereotype.Service
import ru.bodrov.analyzer.csv.CsvReader
import ru.bodrov.analyzer.model.Candle
import ru.bodrov.analyzer.model.Tick
import java.time.LocalDate
import kotlin.time.Duration

@Service
class TickServiceImpl(
    private val csvReader: CsvReader) : TickService {

    override fun filterByTicker(
        ticks: List<Tick>,
        ticker: String
    ): List<Tick> {
        TODO("Not yet implemented")
    }

    override fun filterByPeriod(
        ticks: List<Tick>,
        period: Int
    ): List<Tick> {
        TODO("Not yet implemented")
    }

    override fun sortByDateTime(ticks: List<Tick>): List<Tick> {
        TODO("Not yet implemented")
    }

    override fun filterByDate(
        ticks: List<Tick>,
        from: LocalDate,
        to: LocalDate
    ): List<Tick> {
        TODO("Not yet implemented")
    }

    override fun aggregateToCandles(
        ticks: List<Tick>,
        timeframe: Duration
    ): List<Candle> {
        TODO("Not yet implemented")
    }
}