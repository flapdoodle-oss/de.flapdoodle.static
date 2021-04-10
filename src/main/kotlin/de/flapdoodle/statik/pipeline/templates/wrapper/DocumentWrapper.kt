package de.flapdoodle.statik.pipeline.templates.wrapper

import de.flapdoodle.statik.documents.Document
import de.flapdoodle.statik.filetypes.Attributes

data class DocumentWrapper(private val wrapped: Document): HasAttributes {
    override fun findAttribute(attributeName: Any?, argumentValues: Array<out Any>?): IsAttribute? {
        if (attributeName is String) {
            require(argumentValues==null) {"argumentValues expected to be null: $argumentValues"}

            val resolved = wrapped.allAttributes().find(attributeName, Attributes::class)
            if (resolved != null) {
                return when (resolved) {
                    is Attributes.Node -> IsAttribute(resolved)
                    is Attributes.Values<*> -> IsAttribute(resolved.values)
                }
            }
        }
        return null
    }
}