package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.config.Pages
import de.flapdoodle.statik.documents.DocumentSet

interface PathMapGenerator {
    fun pathMapOf(baseUrl: String, pages: Pages, documents: List<DocumentSet>): Path2PagedDocumentsMap
}