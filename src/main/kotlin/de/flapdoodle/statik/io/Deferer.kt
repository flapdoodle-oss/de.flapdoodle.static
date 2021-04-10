package de.flapdoodle.statik.io

import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference

class Deferer {

    companion object {
        fun <T> onInactivityFor(duration: Long, unit: TimeUnit, delegate: (T) -> Unit): (T) -> Unit {
            return InactivityConsumerDelegate(duration,unit,delegate)
        }
    }

    class InactivityConsumerDelegate<T>(
        duration: Long,
        unit: TimeUnit,
        val delegate: (T) -> Unit
    ): (T) -> Unit {
        private val waitInMs: Long = unit.toMillis(duration)
        private val lastValue = AtomicReference<T>()
        private val lastUpdated = AtomicLong(System.currentTimeMillis())

        private val timeTask: TimerTask = object : TimerTask() {
            override fun run() {
                val timeSinceLastUpdate = System.currentTimeMillis() - lastUpdated.get()
                if (timeSinceLastUpdate > waitInMs) {
                    val value = lastValue.getAndSet(null)
                    if (value != null) {
                        delegate(value)
                    }
                }
            }

        }
        private val timer = Timer("deferer", true)
            .schedule(timeTask, waitInMs, waitInMs/2)

        override fun invoke(value: T) {
            lastValue.set(value)
            lastUpdated.set(System.currentTimeMillis())
        }
    }
}