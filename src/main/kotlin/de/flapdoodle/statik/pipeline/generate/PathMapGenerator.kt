package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.config.Pages
import de.flapdoodle.statik.documents.DocumentSet

interface PathMapGenerator {
    fun pathMapOf(pages: Pages, documents: List<DocumentSet>): Path2PagedDocumentsMap
}