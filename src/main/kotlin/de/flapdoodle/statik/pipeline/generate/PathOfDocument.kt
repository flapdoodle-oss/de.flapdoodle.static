package de.flapdoodle.statik.pipeline.generate

data class PathOfDocument(
    val path: String,
    val page: Int?,
    val maxPage: Int?
)