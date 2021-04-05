package de.flapdoodle.statik.pipeline.templates

import de.flapdoodle.statik.config.Template
import de.flapdoodle.statik.pipeline.templates.pebble.PebbleRenderEngine
import java.nio.file.Path

class AlwaysPebbleRenderEngineFactory : RenderEngineFactory {
    override fun renderEngine(basePath: Path, template: Template): RenderEngine {
        return PebbleRenderEngine(basePath,template)
    }
}