package de.flapdoodle.statik.pipeline.generate

class DefaultObjectFormatter : Formatter {
    override fun format(value: Any): String? {
        return when(value) {
            is String -> value
            is Int -> value.toString()
            is Double -> value.toString()
            else -> null
        }
    }
}