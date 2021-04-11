package de.flapdoodle.statik.pipeline.templates.wrapper

import de.flapdoodle.statik.pipeline.templates.Paging

data class PebbleWrapper(
    val url: String,
    val baseUrl: String,
    val site: SiteWrapper,
    val documents: List<DocumentWrapper>,
    val paging: Paging?
)