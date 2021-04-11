package de.flapdoodle.statik.pipeline.templates

import de.flapdoodle.statik.files.FileSet

interface RenderEngine {
    fun mediaFiles(): List<FileSet>
    fun render(
        template: String,
        renderData: RenderData
    ): String
}