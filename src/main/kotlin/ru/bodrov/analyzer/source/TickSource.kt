package ru.bodrov.analyzer.source

import ru.bodrov.analyzer.model.Tick

interface TickSource {
    fun load(): List<Tick>
}