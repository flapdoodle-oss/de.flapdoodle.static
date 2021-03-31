package de.flapdoodle.statik.pipeline.templates.pebble

import com.mitchellbosecke.pebble.PebbleEngine
import com.mitchellbosecke.pebble.loader.FileLoader
import de.flapdoodle.statik.pipeline.templates.RenderEngine
import de.flapdoodle.statik.pipeline.templates.wrapper.Renderable
import java.io.StringWriter
import java.nio.file.Path

class PebbleRenderEngine(templatePath: Path) : RenderEngine {
    private val loader = FileLoader()
    private val engine = PebbleEngine.Builder()
        .loader(loader)
        .autoEscaping(false)
        .strictVariables(true)
        .extension(CustomExtension())
        .build()

    init {
        loader.prefix = templatePath.toAbsolutePath().toString()
        loader.suffix = ".html"
    }

    override fun render(templatePath: String, renderable: Renderable): String {
        val template = engine.getTemplate(templatePath)
        
        val writer = StringWriter()

        template.evaluate(writer, mutableMapOf("it" to (renderable as Any)))

        return writer.toString()
    }
}