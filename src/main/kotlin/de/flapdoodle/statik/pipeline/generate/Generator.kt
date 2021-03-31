package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.config.Pages
import de.flapdoodle.statik.documents.DocumentSet
import java.nio.file.Path

interface Generator {
    fun generate(
        basePath: Path,
        pages: Pages,
        documents: List<DocumentSet>
    )
}