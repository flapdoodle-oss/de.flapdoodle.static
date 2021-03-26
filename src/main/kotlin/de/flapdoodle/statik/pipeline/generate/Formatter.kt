package de.flapdoodle.statik.pipeline.generate

interface Formatter {
    fun format(value: Any): String?
}