package de.flapdoodle.statik.pipeline.compare

import kotlin.reflect.KClass

object DefaultComparatorLookup : ComparatorLookup {
    override fun comparatorFor(values: Set<Any?>): Comparator<in Any>? {
        return comparableComparator(values)
    }

    // VisibleForTests
    internal fun comparableComparator(values: Set<Any?>): Comparators.GuardedComparator<in Any>? {
        val nonNullValues = values.filterNotNull()
        val comparables: List<Comparable<*>> = nonNullValues.filterIsInstance(Comparable::class.java)
        if (comparables.size == nonNullValues.size) {
            // all values are comparable
            val types: Set<KClass<out Comparable<*>>> = comparables.map { it::class }.toSet()
            if (types.size == 1) {
                // single type
                return comparableComparatorForType(types.single())
            }
        } else {
            if (values.all { it == null }) {
                // everything is null
                return Comparators.guarded({ t1, t2 ->
                    require(t1 == null) {"null expected, but was $t1"}
                    require(t2 == null) {"null expected, but was $t2"}
                    0
                }, Any::class)
            }
        }
        return null
    }

    private fun <T : Comparable<*>> comparableComparatorForType(type: KClass<out T>): Comparators.GuardedComparator<in Any> {
        return Comparators.guarded(genericComparableComparator<Int>() as Comparator<Any>, type)
    }

    private fun <T : Comparable<T>> genericComparableComparator(): Comparator<T> {
        return Comparator.naturalOrder()
    }
}