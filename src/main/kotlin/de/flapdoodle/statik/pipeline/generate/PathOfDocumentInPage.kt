package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.config.Id
import de.flapdoodle.statik.config.PageDefinition
import de.flapdoodle.statik.documents.Document

interface PathOfDocumentInPage {
    fun pathOf(pageDefinition: Id<PageDefinition>, document: Id<Document>): String?
}