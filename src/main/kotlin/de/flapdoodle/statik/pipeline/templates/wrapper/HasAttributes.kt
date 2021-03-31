package de.flapdoodle.statik.pipeline.templates.wrapper

interface HasAttributes {
    fun findAttribute(
        attributeName: Any?,
        argumentValues: Array<out Any>?
    ): IsAttribute?
}