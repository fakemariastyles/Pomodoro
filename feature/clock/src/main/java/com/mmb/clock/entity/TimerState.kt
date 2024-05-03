package com.mmb.clock.entity

data class TimerState(
    val minutes: Long = 0,
    val seconds: Long = 0
) {

    companion object {
        const val SECOND_MILLS = 1000L
        const val SECONDS_PER_MINUTE = 60000L
    }

    fun convertToSeconds(): Long {
        return minutes * SECONDS_PER_MINUTE + seconds * SECOND_MILLS
    }

    override fun toString(): String {
        return "%02d".format(minutes) + ":" + "%02d".format(seconds)
    }
}