package de.flapdoodle.statik.pipeline.templates

data class Paging(
    val current: Page,

    val prev: Page?,
    val next: Page?,

    val first: Page,
    val last: Page
) {
    data class Page(
        val index: Int,
        val path: String
    )

    fun isFirst() = current==first
    fun isLast() = current==last
}