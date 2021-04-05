package de.flapdoodle.statik.pipeline.templates

import de.flapdoodle.statik.config.Template
import java.nio.file.Path

interface RenderEngineFactory {
    fun renderEngine(basePath: Path, template: Template): RenderEngine
}