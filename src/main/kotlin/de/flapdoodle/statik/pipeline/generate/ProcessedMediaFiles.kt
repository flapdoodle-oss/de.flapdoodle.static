package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.types.groupByUnique

class ProcessedMediaFiles(val files: List<MediaFile>) {
    val pathMap = files.groupByUnique { it.path }

    fun find(requestPath: String): MediaFile? {
        return pathMap[requestPath]
    }

    data class MediaFile(
        val path: String,
        val content: ByteArray,
        val mimeType: String = "application/octet-stream"
    )

}