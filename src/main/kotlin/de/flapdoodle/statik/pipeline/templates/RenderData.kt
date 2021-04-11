package de.flapdoodle.statik.pipeline.templates

import de.flapdoodle.statik.config.Site
import de.flapdoodle.statik.documents.Document
import de.flapdoodle.statik.pipeline.generate.PathOfDocumentInPage

data class RenderData(
    val url: String,
    val baseUrl: String,
    val site: Site,
    val documents: List<Document>,
    val pathOfDocumentInPage: PathOfDocumentInPage
)