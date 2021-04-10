package de.flapdoodle.statik.pipeline.templates.wrapper

data class Renderable(
    val url: String,
    val baseUrl: String,
    val site: SiteWrapper,
    val documents: List<DocumentWrapper>
)