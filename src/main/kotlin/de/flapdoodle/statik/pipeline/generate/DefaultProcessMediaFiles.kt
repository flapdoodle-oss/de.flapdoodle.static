package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.files.FileSet
import de.flapdoodle.statik.files.Reference
import java.nio.file.Files
import java.nio.file.Path

class DefaultProcessMediaFiles : ProcessMediaFiles {
    override fun process(baseUrl: String, fileSets: List<FileSet>): ProcessedMediaFiles {
        val files = fileSets.flatMap { fileSet ->
            fileSet.nodes.map { file ->
                val content = contentOf(file)
                ProcessedMediaFiles.MediaFile(
                    baseUrl + asRelativeUrl(fileSet.basePath, file.path),
                    content,
                )
            }
        }
        return ProcessedMediaFiles(files)
    }

    private fun asRelativeUrl(basePath: Path, path: Path): String {
        val relativePath = basePath.relativize(path)
        return "/" + relativePath.map { it.fileName }.joinToString("/")
    }

    private fun contentOf(reference: Reference): ByteArray {
        return when (reference) {
            is Reference.File -> Files.readAllBytes(reference.path)
            is Reference.SymLink -> Files.readAllBytes(reference.destination)
        }
    }

}