package com.x2mobile.wodjar.util

import java.util.concurrent.TimeUnit


class TimeUtil {

    companion object {

        @JvmStatic
        fun formatTime(time: Long): String {
            val hour = TimeUnit.SECONDS.toHours(time)
            val minute = TimeUnit.SECONDS.toMinutes(time - TimeUnit.HOURS.toSeconds(hour))
            val seconds = time - TimeUnit.HOURS.toSeconds(hour) - TimeUnit.MINUTES.toSeconds(minute)
            return String.format("%02d:%02d:%02d", hour, minute, seconds)
        }
    }
}