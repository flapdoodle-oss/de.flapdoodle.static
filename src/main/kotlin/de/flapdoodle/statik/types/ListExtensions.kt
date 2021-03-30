package de.flapdoodle.statik.types

fun <T> List<T>.asSet(): Set<T> {
    val set = toSet()
    require(set.size==size) {"value collision: $this"}
    return set
}

fun <T> List<T>.head(): T {
    require(size>0) {"list is empty"}
    return this[0]
}

fun <T> List<T>.tail(): List<T> {
    require(size>0) {"list is empty"}
    return subList(1,size)
}

fun <T,R> List<T>.groupByUnique(keySelector: (T) -> R): Map<R, T> {
    return groupingBy(keySelector)
        .reduce { key, _, element -> error("Duplicate key '$key' encountered for element '$element'") }
}