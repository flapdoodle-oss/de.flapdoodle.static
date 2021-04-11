package de.flapdoodle.statik.pipeline.templates.wrapper

import de.flapdoodle.statik.config.Id
import de.flapdoodle.statik.config.PageDefinition
import de.flapdoodle.statik.documents.Document
import de.flapdoodle.statik.filetypes.Attributes
import de.flapdoodle.statik.pipeline.generate.PathOfDocumentInPage

data class DocumentWrapper(
    private val wrapped: Document,
    private val pathOfDocumentInPage: PathOfDocumentInPage
) : HasAttributes {
    override fun findAttribute(attributeName: Any?, argumentValues: Array<out Any>?): IsAttribute? {
        if (attributeName is String && argumentValues == null) {
//            require(argumentValues==null) {"argumentValues expected to be null: $argumentValues"}

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

    fun pathToPage(pageDefinition: String?): String? {
        if (pageDefinition!=null) {
            return pathOfDocumentInPage.pathOf(Id(pageDefinition, PageDefinition::class), Id(wrapped.id, Document::class))
        }
        return null
    }

    override fun toString(): String {
        return "DocumentWrapper($wrapped)"
    }
}