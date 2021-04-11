package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.config.Pages
import de.flapdoodle.statik.config.Site
import de.flapdoodle.statik.documents.Document
import de.flapdoodle.statik.documents.DocumentSet
import de.flapdoodle.statik.pipeline.templates.RenderEngine
import de.flapdoodle.statik.pipeline.templates.wrapper.Renderable
import de.flapdoodle.statik.pipeline.templates.wrapper.RenderableFactory
import java.nio.file.Path

interface Generator {
    fun generate(
        baseUrl: String,
        basePath: Path,
        pages: Pages,
        site: Site,
        documents: List<DocumentSet>,
        renderEngine: RenderEngine
    ): RendererPages
}