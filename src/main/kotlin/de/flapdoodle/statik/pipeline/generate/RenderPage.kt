package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.config.PageDefinition
import de.flapdoodle.statik.documents.Document
import de.flapdoodle.statik.path.Path

interface RenderPage {
    fun render(
        path: String,
        // TODO pageNr??
        pageDefinition: PageDefinition,
        documents: List<Document>
    ): String
}