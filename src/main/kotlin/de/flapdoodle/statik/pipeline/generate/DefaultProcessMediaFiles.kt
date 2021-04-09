package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.files.FileSet
import de.flapdoodle.statik.files.Reference
import org.apache.tika.config.TikaConfig
import org.apache.tika.metadata.Metadata
import org.apache.tika.mime.MediaType
import java.io.ByteArrayInputStream
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
                    mimeType = guessMimeType(content, file.path.fileName?.toString())
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

    companion object {
        val tikaConfig = TikaConfig.getDefaultConfig()
        val detector = tikaConfig.detector

        private fun guessMimeType(content: ByteArray, fileName: String?): String {
            if (fileName!=null) {
                when (fileName.substringAfterLast('.', "")) {
                    "css" -> return "text/css"
                    "js" -> return "text/javascript"
                }
            }

            val result: MediaType? = detector.detect(ByteArrayInputStream(content), Metadata())
            if (result!=null) {
                return result.type+"/"+result.subtype
            }
            return "application/octet-stream"
        }
    }
}