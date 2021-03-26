package de.flapdoodle.statik.regex

import de.flapdoodle.statik.types.Either
import java.util.function.Consumer
import java.util.regex.Matcher
import java.util.regex.Pattern

object Patterns {
    fun parse(pattern: Pattern, src: String, consumer: (Either<String, Matcher>) -> Unit) {
        val matcher = pattern.matcher(src)
        var lastEnd = 0
        while (matcher.find()) {
            consumer(Either.left(src.substring(lastEnd, matcher.start())))
            consumer(Either.right(matcher))
            lastEnd = matcher.end()
        }
        consumer(Either.left(src.substring(lastEnd)))
    }
}