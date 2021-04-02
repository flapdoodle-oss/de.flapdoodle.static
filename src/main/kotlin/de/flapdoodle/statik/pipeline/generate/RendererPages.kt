package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.types.groupByUnique

data class RendererPages(val pages: List<Page>) {
    val pageMap = pages.groupByUnique { it.path }

    fun find(requestPath: String): Page? {
        return pageMap[requestPath]
    }

    data class Page(
        val path: String,
        val content: String,
        val mimeType: String = "text/html"
    )
}