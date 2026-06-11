package com.edu.app.domain.srs

enum class ReviewGrade(val q: Int) { AGAIN(0), HARD(3), GOOD(4), EASY(5) }

data class SrsState(val easeFactor: Float, val intervalDays: Int, val repetitions: Int)

data class SrsUpdate(val state: SrsState, val intervalDays: Int, val nextDueAtMillis: Long)

object SpacedRepetition {
    private const val MIN_EASE = 1.3f
    private const val DAY_MILLIS = 24L * 60 * 60 * 1000

    fun review(current: SrsState, grade: ReviewGrade, nowMillis: Long): SrsUpdate {
        val q = grade.q
        val newEase = (current.easeFactor + (0.1f - (5 - q) * (0.08f + (5 - q) * 0.02f)))
            .coerceAtLeast(MIN_EASE)

        val (newReps, newInterval) = if (q < ReviewGrade.GOOD.q) {
            0 to 1
        } else {
            when (current.repetitions) {
                0 -> 1 to 1
                1 -> 2 to 6
                else -> (current.repetitions + 1) to
                        Math.round(current.intervalDays * newEase).coerceAtLeast(1)
            }
        }
        return SrsUpdate(
            state = SrsState(newEase, newInterval, newReps),
            intervalDays = newInterval,
            nextDueAtMillis = nowMillis + newInterval * DAY_MILLIS
        )
    }
}
