package de.flapdoodle.static.io

import java.nio.file.FileVisitOption
import java.nio.file.Files
import java.nio.file.Path

object IO {

    fun scan(path: Path, onFileTreeEvent: OnFileTreeEvent) {
        Files.walkFileTree(
            path,
            setOf(FileVisitOption.FOLLOW_LINKS),
            Int.MAX_VALUE,
            Visitor2EventAdapter(onFileTreeEvent)
        )
    }
}