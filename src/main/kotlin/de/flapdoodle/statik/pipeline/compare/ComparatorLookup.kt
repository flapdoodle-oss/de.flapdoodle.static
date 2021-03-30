package de.flapdoodle.statik.pipeline.compare

interface ComparatorLookup {
    fun comparatorFor(values: Set<Any?>): Comparator<in Any>?
}