package de.flapdoodle.statik.di

import de.flapdoodle.statik.documents.DocumentFromReference
import de.flapdoodle.statik.documents.MetaInHeadDocumentFromReferenceParser
import de.flapdoodle.statik.pipeline.documents.DocumentSetsFromFileSets
import de.flapdoodle.statik.pipeline.documents.DocumentSetsFromFileSetsMapper
import de.flapdoodle.statik.pipeline.files.ReadFileSets
import de.flapdoodle.statik.pipeline.files.ReadFileSetsFromFS
import de.flapdoodle.statik.pipeline.generate.*
import de.flapdoodle.statik.pipeline.templates.AlwaysPebbleRenderEngineFactory
import de.flapdoodle.statik.pipeline.templates.RenderEngineFactory
import java.nio.file.Path

class PipelineModule(val preview: Boolean, val destination: Path) : KModule() {

    override fun configure() {
        bind(ReadFileSets::class).with(ReadFileSetsFromFS::class)
        bind(DocumentFromReference::class).toInstance(MetaInHeadDocumentFromReferenceParser)
        bind(DocumentSetsFromFileSets::class).with(DocumentSetsFromFileSetsMapper::class)

        bind(PathMapGenerator::class).with(DefaultPathMapGenerator::class)
        bind(RenderEngineFactory::class).with(AlwaysPebbleRenderEngineFactory::class)
        bind(Generator::class).with(DummyGenerator::class)

        bind(ProcessMediaFiles::class).with(DefaultProcessMediaFiles::class)
        
        if (preview) {
            install(PreviewPipelineModule())
        } else {
            install(DefaultPipelineModule(destination))
        }
    }
}