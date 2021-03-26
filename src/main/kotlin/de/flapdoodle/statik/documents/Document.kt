package de.flapdoodle.statik.documents

import de.flapdoodle.statik.files.ContentType
import de.flapdoodle.statik.files.Reference
import de.flapdoodle.statik.filetypes.Attributes

data class Document(
    val reference: Reference,
    val attributes: Attributes.Node,
    val contentType: ContentType,
    val content: String
) {
    fun allAttributes() = attributes + reference.attributes
}