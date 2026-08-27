package ru.bodrov.analyzer.common

import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

enum class Timeframe(val period: Long) {
    M1(1),
    M5(5),
    M15(15),
    H1(1),
    D1(1),
    W1(1),
    MN1(1);

    fun calculateEndTime(startTime: LocalDateTime): LocalDateTime =
        when (this) {
            M1 -> startTime.plusMinutes(M1.period).minusSeconds(1)
            M5 -> startTime.plusMinutes(M5.period).minusSeconds(1)
            M15 -> startTime.plusMinutes(M15.period).minusSeconds(1)
            H1 -> startTime.plusHours(H1.period).minusSeconds(1)
            D1 -> startTime.plusDays(D1.period).minusSeconds(1)
            W1 -> startTime.plusWeeks(W1.period).minusSeconds(1)
            MN1 -> startTime.plusMonths(MN1.period).minusSeconds(1)
        }

    fun calculateStartTime(timeTick: LocalDateTime, startTrade: LocalDateTime): LocalDateTime {
        val duration = Duration.between(startTrade, timeTick)
        val durationFrame = when (this) {
            M1, M5, M15 -> duration.toMinutes()
            H1 -> duration.toHours()
            D1 -> duration.toDays()
            W1 -> duration.toDays() / 7
            MN1 -> ChronoUnit.MONTHS.between(startTrade, timeTick)
        }

        val count = (durationFrame / this.period) * this.period

        return when (this) {
            M1, M5, M15 -> startTrade.plusMinutes(count)
            H1 -> startTrade.plusHours(count)
            D1 -> startTrade.plusDays(count)
            W1 -> startTrade.plusWeeks(count)
            MN1 -> startTrade.plusMonths(count)
        }
    }
}