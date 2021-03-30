package de.flapdoodle.statik.config

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KClass

data class Id<T : Any>(val id: String, val type: KClass<out T>) {
    companion object {
        private val idGeneratorMap = ConcurrentHashMap<KClass<out Any>, AtomicInteger>()

        private fun nextIdFor(type: KClass<out Any>): Int {
            return idGeneratorMap.getOrPut(type, { AtomicInteger() }).incrementAndGet()
        }

        fun <T: Any> create(type: KClass<T>): String {
            return nextIdFor(type).toString()
        }

        fun <T: Any>of(it: T, idExtractor: (T) -> String): Id<T> {
            return Id(idExtractor(it), it::class)
        }
    }
}