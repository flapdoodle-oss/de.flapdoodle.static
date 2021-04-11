package de.flapdoodle.statik.pipeline.templates

import de.flapdoodle.statik.files.FileSet
import de.flapdoodle.statik.pipeline.templates.wrapper.Renderable

interface RenderEngine {
    fun mediaFiles(): List<FileSet>
    fun render(
        template: String,
        renderData: RenderData
    ): String
}