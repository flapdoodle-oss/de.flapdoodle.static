package de.flapdoodle.statik.pipeline

import com.google.inject.Inject
import de.flapdoodle.statik.config.Config
import de.flapdoodle.statik.documents.MetaInHeadDocumentFromReferenceParser
import de.flapdoodle.statik.files.FileType
import de.flapdoodle.statik.pipeline.documents.DocumentSetsFromFileSets
import de.flapdoodle.statik.pipeline.documents.DocumentSetsFromFileSetsMapper
import de.flapdoodle.statik.pipeline.files.ReadFileSets
import de.flapdoodle.statik.pipeline.files.ReadFileSetsFromFS
import de.flapdoodle.statik.pipeline.generate.DummyGenerator
import de.flapdoodle.statik.pipeline.generate.Generator
import de.flapdoodle.statik.pipeline.generate.ProcessMediaFiles
import de.flapdoodle.statik.pipeline.publish.Dump2ConsolePublisher
import de.flapdoodle.statik.pipeline.publish.Publisher
import de.flapdoodle.statik.pipeline.publish.UndertowPublisher
import de.flapdoodle.statik.pipeline.templates.RenderEngineFactory
import de.flapdoodle.statik.pipeline.templates.wrapper.DocumentWrapper
import de.flapdoodle.statik.pipeline.templates.wrapper.Renderable
import de.flapdoodle.statik.pipeline.templates.wrapper.SiteWrapper

class Pipeline @Inject constructor(
    private val readFileSets: ReadFileSets,// = ReadFileSetsFromFS(),
    private val documentsDocumentSetsFromFileSets: DocumentSetsFromFileSets, // = DocumentSetsFromFileSetsMapper(
//        documentFromReference = MetaInHeadDocumentFromReferenceParser
//    ),
    private val generator: Generator, // = DummyGenerator(),
    private val renderEngineFactory: RenderEngineFactory,
    private val processMediaFiles: ProcessMediaFiles,
    private val publisher: Publisher // = Dump2ConsolePublisher()
) {
    fun process(config: Config) {
        val fileSets = readFileSets.read(config.sources)
        val documents = documentsDocumentSetsFromFileSets.scan(fileSets)

        println("--------------")
        println("file sets:")
        fileSets.forEach { println(it) }

        println("--------------")
        println("documents:")
        documents.forEach { println(it) }


        val renderEngine = renderEngineFactory.renderEngine(config.basePath, config.template)
        val rendererPages = generator.generate(publisher.baseUrl(), config.basePath, config.pages, documents, renderEngine) { path, documents ->
            Renderable(
                url = path,
                baseUrl = publisher.baseUrl(),
                site = SiteWrapper(config.site),
                documents = documents.map(::DocumentWrapper)
            )
        }
        val processedMediaFiles = processMediaFiles.process(
            publisher.baseUrl(),
            renderEngine.mediaFiles() +
                    fileSets.filter { it.type==FileType.Binary }
        )

        publisher.publish(rendererPages, processedMediaFiles)
    }
}