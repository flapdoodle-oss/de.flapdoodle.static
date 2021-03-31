package de.flapdoodle.statik.pipeline

import de.flapdoodle.statik.config.Config
import de.flapdoodle.statik.documents.MetaInHeadDocumentFromReferenceParser
import de.flapdoodle.statik.pipeline.documents.DocumentSetsFromFileSets
import de.flapdoodle.statik.pipeline.documents.DocumentSetsFromFileSetsMapper
import de.flapdoodle.statik.pipeline.files.ReadFileSets
import de.flapdoodle.statik.pipeline.files.ReadFileSetsFromFS
import de.flapdoodle.statik.pipeline.generate.DummyGenerator
import de.flapdoodle.statik.pipeline.generate.Generator
import de.flapdoodle.statik.pipeline.templates.wrapper.Renderable
import de.flapdoodle.statik.pipeline.templates.wrapper.SiteWrapper

class Pipeline(
    private val readFileSets: ReadFileSets = ReadFileSetsFromFS(),
    private val documentsDocumentSetsFromFileSets: DocumentSetsFromFileSets = DocumentSetsFromFileSetsMapper(
        documentFromReference = MetaInHeadDocumentFromReferenceParser
    ),
    private val generator: Generator = DummyGenerator()
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

        generator.generate(config.basePath, config.pages, documents) { path, documents ->
            Renderable(
                url = path,
                baseUrl = "foo",
                site = SiteWrapper(config.site),
                documents = documents
            )
        }
    }
}