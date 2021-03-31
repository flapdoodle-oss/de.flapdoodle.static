package de.flapdoodle.statik.pipeline.templates

import java.nio.file.Path

interface RenderEngineFactory {
    fun renderEngine(templatePath: Path): RenderEngine
}