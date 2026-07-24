package ru.bodrov.analyzer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.io.ClassPathResource
import ru.bodrov.analyzer.csv.CsvReader
import java.io.File

@SpringBootApplication
class AnalyzerApplication

fun main(args: Array<String>) {
	runApplication<AnalyzerApplication>(*args)
}
