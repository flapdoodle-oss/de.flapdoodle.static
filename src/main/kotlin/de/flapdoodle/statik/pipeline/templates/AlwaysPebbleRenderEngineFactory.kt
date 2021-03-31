package de.flapdoodle.statik.pipeline.templates

import de.flapdoodle.statik.pipeline.templates.pebble.PebbleRenderEngine
import java.nio.file.Path

class AlwaysPebbleRenderEngineFactory : RenderEngineFactory {
    override fun renderEngine(templatePath: Path): RenderEngine {
        return PebbleRenderEngine(templatePath)
    }
}