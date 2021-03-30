package de.flapdoodle.statik.documents

import de.flapdoodle.statik.config.Id
import de.flapdoodle.statik.files.ContentType
import de.flapdoodle.statik.files.Reference
import de.flapdoodle.statik.filetypes.Attributes

data class Document(
    val id: String = Id.create(Document::class),
    val reference: Reference,
    val attributes: Attributes.Node,
    val contentType: ContentType,
    val content: String
) {
    private val allAttributes = attributes + reference.attributes
    
    fun allAttributes() = allAttributes
}