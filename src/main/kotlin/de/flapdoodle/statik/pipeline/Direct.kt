package de.flapdoodle.statik.pipeline

import de.flapdoodle.statik.config.Config
import de.flapdoodle.statik.pipeline.files.ReadFileSets
import de.flapdoodle.statik.pipeline.files.ReadFileSetsFromFS

class Direct(
    val readFileSets: ReadFileSets = ReadFileSetsFromFS()
) {
    fun process(config: Config) {
        val fileSets = readFileSets.read(config.sources)
        println("fileSets: $fileSets")
    }
}