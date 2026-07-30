package ru.bodrov.analyzer.source

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import ru.bodrov.analyzer.csv.CsvReader
import ru.bodrov.analyzer.model.Tick

@Component
class CsvTickSource (
    private val csvReader: CsvReader,
    @Value("\${source.GC_260716_260716.csv}") private val fileResource: Resource
) : TickSource {

    override fun load(): List<Tick> = csvReader.readTicks(fileResource.inputStream)
}