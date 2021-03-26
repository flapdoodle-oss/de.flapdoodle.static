package de.flapdoodle.statik.types

fun <T> List<T>.asSet(): Set<T> {
    val set = toSet()
    require(set.size==size) {"value collision: $this"}
    return set
}