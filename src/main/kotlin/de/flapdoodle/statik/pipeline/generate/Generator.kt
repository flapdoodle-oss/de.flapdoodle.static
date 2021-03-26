package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.config.Pages
import de.flapdoodle.statik.documents.DocumentSet

interface Generator {
    fun generate(
        pages: Pages,
        documents: List<DocumentSet>
    )
}