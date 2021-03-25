package de.flapdoodle.statik.files

import de.flapdoodle.statik.filetypes.Attributes

data class Document(
    val reference: Reference,
    val attributes: Attributes.Node,
    val contentType: ContentType,
    val content: String
)