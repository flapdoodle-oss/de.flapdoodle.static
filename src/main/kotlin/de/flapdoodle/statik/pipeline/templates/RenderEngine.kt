package de.flapdoodle.statik.pipeline.templates

interface RenderEngine {
    fun render(template: String, renderable: Renderable): String
}