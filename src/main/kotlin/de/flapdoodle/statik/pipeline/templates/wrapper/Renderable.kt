package de.flapdoodle.statik.pipeline.templates.wrapper

import de.flapdoodle.statik.config.Site
import de.flapdoodle.statik.documents.Document

data class Renderable(
    val url: String,
    val baseUrl: String,
    val site: SiteWrapper,
    val documents: List<Document>
)