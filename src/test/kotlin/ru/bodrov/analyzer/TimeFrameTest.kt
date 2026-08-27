package ru.bodrov.analyzer

import ru.bodrov.analyzer.common.Timeframe
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class TimeFrameTest {

    @Test
    fun `M5 should calculate start time and end time`() {
        val timeframe = Timeframe.M5
        val startTrade = LocalDateTime.of(2026, 1, 1, 10, 0, 0)
        val tickTime = LocalDateTime.of(2026, 1, 1, 10, 4, 59)
        val startTick = timeframe.calculateStartTime(tickTime, startTrade)
        assertEquals(startTick, startTrade)

        val endTick = timeframe.calculateEndTime(startTick)
        assertEquals(endTick, startTrade.plusMinutes(Timeframe.M5.period).minusSeconds(1))
    }

    @Test
    fun `M15 should calculate start time and end time`() {
        val timeframe = Timeframe.M15
        val startTrade = LocalDateTime.of(2026, 1, 1, 10, 0, 0)
        val tickTime = LocalDateTime.of(2026, 1, 1, 15, 5, 59)
        val startTick = timeframe.calculateStartTime(tickTime, startTrade)
        assertEquals(startTick, startTrade.plusHours(5))

        val endTick = timeframe.calculateEndTime(startTick)
        assertEquals(endTick, startTick.plusMinutes(Timeframe.M15.period).minusSeconds(1))
    }

    @Test
    fun `H1 should calculate start time and end time`() {
        val timeframe = Timeframe.H1
        val startTrade = LocalDateTime.of(2026, 1, 1, 10, 0, 0)
        val tickTime = LocalDateTime.of(2026, 1, 1, 15, 45, 5)
        val startTick = timeframe.calculateStartTime(tickTime, startTrade)
        assertEquals(startTick, startTrade.plusHours(5))

        val endTick = timeframe.calculateEndTime(startTick)
        assertEquals(endTick, startTick.plusHours(Timeframe.H1.period).minusSeconds(1))
    }

    @Test
    fun `D1 should calculate start time and end time`() {
        val timeframe = Timeframe.D1
        val startTrade = LocalDateTime.of(2026, 1, 1, 10, 0, 0)
        val tickTime = LocalDateTime.of(2026, 1, 4, 15, 45, 5)
        val startTick = timeframe.calculateStartTime(tickTime, startTrade)
        assertEquals(startTick, startTrade.plusDays(3))

        val endTick = timeframe.calculateEndTime(startTick)
        assertEquals(endTick, startTick.plusDays(Timeframe.D1.period).minusSeconds(1))
    }

    @Test
    fun `W1 should calculate start time and end time`() {
        val timeframe = Timeframe.W1
        val startTrade = LocalDateTime.of(2026, 1, 1, 10, 0, 0)
        val tickTime = LocalDateTime.of(2026, 1, 24, 15, 45, 5)
        val startTick = timeframe.calculateStartTime(tickTime, startTrade)
        assertEquals(startTick, startTrade.plusWeeks(3))

        val endTick = timeframe.calculateEndTime(startTick)
        assertEquals(endTick, startTick.plusWeeks(Timeframe.W1.period).minusSeconds(1))
    }

    @Test
    fun `MN1 should calculate start time and end time`() {
        val timeframe = Timeframe.MN1
        val startTrade = LocalDateTime.of(2026, 1, 1, 10, 0, 0)
        val tickTime = LocalDateTime.of(2026, 5, 24, 15, 45, 5)
        val startTick = timeframe.calculateStartTime(tickTime, startTrade)
        assertEquals(startTick, startTrade.plusMonths(4))

        val endTick = timeframe.calculateEndTime(startTick)
        assertEquals(endTick, startTick.plusMonths(Timeframe.MN1.period).minusSeconds(1))

    }

    @Test
    fun `M5 tick exactly at boundary should start new candle`() {
        val startTrade = LocalDateTime.of(2026, 1, 1, 10, 0)
        val tickTime = LocalDateTime.of(2026, 1, 1, 10, 5)

        val result = Timeframe.M5.calculateStartTime(
            tickTime,
            startTrade
        )

        assertEquals(
            LocalDateTime.of(2026, 1, 1, 10, 5),
            result
        )
    }

    @Test
    fun `M15 tick exactly at boundary should start new candle`() {
        val startTrade = LocalDateTime.of(2026, 1, 1, 10, 0)
        val tickTime = LocalDateTime.of(2026, 1, 1, 10, 15)

        val result = Timeframe.M15.calculateStartTime(
            tickTime,
            startTrade
        )

        assertEquals(
            LocalDateTime.of(2026, 1, 1, 10, 15),
            result
        )
    }

    @Test
    fun `H1 tick exactly at boundary should start new candle`() {
        val startTrade = LocalDateTime.of(2026, 1, 1, 10, 0)
        val tickTime = LocalDateTime.of(2026, 1, 1, 11, 0)

        val result = Timeframe.H1.calculateStartTime(
            tickTime,
            startTrade
        )

        assertEquals(
            LocalDateTime.of(2026, 1, 1, 11, 0),
            result
        )
    }

    @Test
    fun `D1 tick exactly at boundary should start new candle`() {
        val startTrade = LocalDateTime.of(2026, 1, 1, 10, 0)
        val tickTime = LocalDateTime.of(2026, 1, 2, 10, 0)

        val result = Timeframe.D1.calculateStartTime(
            tickTime,
            startTrade
        )

        assertEquals(
            LocalDateTime.of(2026, 1, 2, 10, 0),
            result
        )
    }

    @Test
    fun `W1 tick exactly at boundary should start new candle`() {
        val startTrade = LocalDateTime.of(2026, 1, 1, 10, 0)
        val tickTime = LocalDateTime.of(2026, 1, 8, 10, 0)

        val result = Timeframe.W1.calculateStartTime(
            tickTime,
            startTrade
        )

        assertEquals(
            LocalDateTime.of(2026, 1, 8, 10, 0),
            result
        )
    }

    @Test
    fun `MN1 tick exactly at boundary should start new candle`() {
        val startTrade = LocalDateTime.of(2026, 1, 1, 10, 0)
        val tickTime = LocalDateTime.of(2026, 2, 1, 10, 0)

        val result = Timeframe.MN1.calculateStartTime(
            tickTime,
            startTrade
        )

        assertEquals(
            LocalDateTime.of(2026, 2, 1, 10, 0),
            result
        )
    }

    @Test
    fun `MN1 tick 1 seconds to boundary should start new candle`() {
        val startTrade = LocalDateTime.of(2026, 1, 1, 10, 0)
        val tickTime = LocalDateTime.of(2026, 2, 1, 9, 59)

        val result = Timeframe.MN1.calculateStartTime(
            tickTime,
            startTrade
        )

        assertEquals(
            LocalDateTime.of(2026, 1, 1, 10, 0),
            result
        )
    }
}