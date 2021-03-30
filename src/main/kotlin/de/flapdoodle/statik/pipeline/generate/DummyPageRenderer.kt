package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.config.PageDefinition
import de.flapdoodle.statik.documents.Document

class DummyPageRenderer : RenderPage {
    override fun render(path: String, pageDefinition: PageDefinition, documents: List<Document>): String {
        return documents.map {
            it.content
        }.joinToString(separator = "-----\n")
    }
}