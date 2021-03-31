package de.flapdoodle.statik.pipeline.templates

import de.flapdoodle.statik.pipeline.templates.wrapper.Renderable

interface RenderEngine {
    fun render(template: String, renderable: Renderable): String
}