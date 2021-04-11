package de.flapdoodle.statik.pipeline.templates.pebble

import com.mitchellbosecke.pebble.PebbleEngine
import com.mitchellbosecke.pebble.error.PebbleException
import com.mitchellbosecke.pebble.loader.FileLoader
import de.flapdoodle.statik.config.Template
import de.flapdoodle.statik.files.CollectFileSet
import de.flapdoodle.statik.files.FileSet
import de.flapdoodle.statik.files.FileType
import de.flapdoodle.statik.pipeline.ProcessPipelineException
import de.flapdoodle.statik.pipeline.templates.RenderData
import de.flapdoodle.statik.pipeline.templates.RenderEngine
import de.flapdoodle.statik.pipeline.templates.wrapper.DocumentWrapper
import de.flapdoodle.statik.pipeline.templates.wrapper.Renderable
import de.flapdoodle.statik.pipeline.templates.wrapper.SiteWrapper
import java.io.StringWriter
import java.nio.file.Path

class PebbleRenderEngine(
    basePath: Path,
    private val template: Template
) : RenderEngine {
    private val templatePath = basePath.resolve(template.path)
    
    private val loader = FileLoader()
    private val engine = PebbleEngine.Builder()
        .loader(loader)
        .autoEscaping(false)
        .strictVariables(true)
        .extension(CustomExtension())
        .build()

    init {
        loader.prefix = templatePath.toAbsolutePath().toString()
        loader.suffix = ".peb"
    }

    override fun render(templatePath: String, renderData: RenderData): String {
        val writer = StringWriter()
        try {
            val template = engine.getTemplate(templatePath)

            val wrapper = Renderable(
                url = renderData.url,
                baseUrl = renderData.baseUrl,
                documents = renderData.documents.map { DocumentWrapper(it, renderData.pathOfDocumentInPage) },
                site = SiteWrapper(renderData.site),
            )

            template.evaluate(writer, mutableMapOf("it" to (wrapper as Any)))
        } catch (ex: PebbleException) {
            throw ProcessPipelineException("could not render $renderData with $templatePath", ex)
        }

        return writer.toString()
    }

    override fun mediaFiles(): List<FileSet> {
        return template.resources.map {
            CollectFileSet.read(templatePath, it.id, it.paths, FileType.Binary)
        }
    }
}