package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.config.Pages
import de.flapdoodle.statik.documents.Document
import de.flapdoodle.statik.documents.DocumentSet
import de.flapdoodle.statik.pipeline.templates.RenderEngine
import de.flapdoodle.statik.pipeline.templates.wrapper.Renderable
import java.nio.file.Path

interface Generator {
    fun generate(
        baseUrl: String,
        basePath: Path,
        pages: Pages,
        documents: List<DocumentSet>,
        renderEngine: RenderEngine,
        renderableFactory: (path: String, documents: List<Document>) -> Renderable
    ): RendererPages
}