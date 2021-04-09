package de.flapdoodle.statik.io

import java.io.IOException
import java.lang.IllegalArgumentException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.concurrent.TimeUnit
import kotlin.io.path.isDirectory

class PathWatcher(val path: Path) {
    private val watcher: WatchService = FileSystems.getDefault().newWatchService()
//    private var watchKeyMap: Map<WatchKey, Path> = emptyMap()

    init {
//        require(!Files.isSymbolicLink(path)) {"path is symlink: $path"}
        registerTree(path)
    }

    private fun registerTree(current: Path) {
        if (Files.isDirectory(current)) {
            Files.walkFileTree(current, setOf(FileVisitOption.FOLLOW_LINKS),1000, object : SimpleFileVisitor<Path>() {
                override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                    register(watcher, dir)

                    return FileVisitResult.CONTINUE
                }
            })
        }
    }

    fun processEvents(duration: Long, unit: TimeUnit, eventListener: (List<Event>) -> Unit) {
        var key: WatchKey?


        do {
            var eventList = emptyList<Event>()

            key = watcher.poll(duration, unit)
            if (key != null) {
                val currentPath = key.watchable() as Path
                val events = key.pollEvents() as List<WatchEvent<Path>>
                events.forEach { event ->
                    val path = currentPath.resolve(event.context())
                    val kind = event.kind()
                    when (kind) {
                        StandardWatchEventKinds.ENTRY_CREATE -> {
                            eventList = eventList + Event.Created(path)
//                            println("create $path")
                            registerTree(path)
                        }
                        StandardWatchEventKinds.ENTRY_DELETE -> {
                            eventList = eventList + Event.Deleted(path)
//                            println("delete $path")
                        }
                        StandardWatchEventKinds.ENTRY_MODIFY -> {
                            eventList = eventList + Event.Modified(path)
//                            println("modify $path")
                            registerTree(path)
                        }
                        else -> throw IllegalArgumentException("unsupported: $kind on $path")
                    }
                }

                val couldReset = key.reset()

//                println("could reset key for ${key.watchable()}")
            }

            if (!eventList.isEmpty()) {
                eventListener(eventList)
            }
        } while (key!=null)

    }

    sealed class Event(open val path: Path) {
        data class Created(override val path: Path) : Event(path)
        data class Modified(override val path: Path) : Event(path)
        data class Deleted(override val path: Path) : Event(path)
    }

    companion object {
        @Throws(IOException::class)
        private fun register(watcher: WatchService, dir: Path): WatchKey {
            return dir.register(
                watcher,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY
            )
        }

        fun watch(path: Path, duration: Long, unit: TimeUnit, eventListener: (List<Event>) -> Unit): Thread {
            val pathWatcher = PathWatcher(path)
            val thread = Thread {
                try {
                    while (!Thread.currentThread().isInterrupted) {
                        pathWatcher.processEvents(duration, unit, eventListener)
                    }
                } catch (ex: InterruptedException) {
                    println("stop....")
                }
            }
            thread.isDaemon = true
            thread.start()

            return thread
        }
    }

}