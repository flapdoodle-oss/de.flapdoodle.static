package de.flapdoodle.statik.types

fun <T,R> Iterable<T>.groupByUnique(keySelector: (T) -> R): Map<R, T> {
    return groupingBy(keySelector)
        .reduce { key, _, element -> error("Duplicate key '$key' encountered for element '$element'") }
}