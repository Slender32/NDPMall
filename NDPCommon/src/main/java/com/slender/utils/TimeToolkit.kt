package com.slender.utils

object TimeToolkit{
    interface Unit {
        companion object {
            const val WEEK: Int = 604800000
            const val DAY: Int = 86400000
            const val HOUR: Int = 3600000
            const val MINUTE: Int = 60000
            const val SECOND: Int = 1000
        }
    }
}
