package de.flapdoodle.statik.io

import com.google.common.base.Charsets
import de.flapdoodle.statik.io.PathWatcher.Event.*
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

internal class PathWatcherTest {

    @Test
    fun watcherMustCallEventListenerWithRecordedEvents() {
        var recordedEvents = emptyList<List<PathWatcher.Event>>()
        val eventListener: (List<PathWatcher.Event>) -> Unit = { events ->
            recordedEvents = recordedEvents.plusElement(events)
        }

        FilesInTests.withTempDirectory("watcher") {
            val testee = PathWatcher(current)

            val sub = mkDir("sub")
            testee.processEvents(100, TimeUnit.MILLISECONDS, eventListener)
            val otherSub = sub.mkDir("other")
            testee.processEvents(100, TimeUnit.MILLISECONDS, eventListener)
            sub.delete()

            val file = createFile("sample", "foo")

            testee.processEvents(100, TimeUnit.MILLISECONDS, eventListener)

            assertThat(recordedEvents).containsExactly(
                listOf(
                    Created(current.resolve("sub"))
                ),
                listOf(
                    Created(current.resolve("sub").resolve("other"))
                ),
                listOf(
                    Deleted(current.resolve("sub").resolve("other"))
                ),
                listOf(
                    Deleted(current.resolve("sub")),
                    Created(current.resolve("sample")),
                    Modified(current.resolve("sample")),
                )
            )
        }

    }

    @Test
    @Disabled
    @Throws(InterruptedException::class, ExecutionException::class)
    fun watcherMustNotify(@TempDir tempDir: Path) {
        val shouldCreateFiles = AtomicBoolean(true)
        val changeCounter = AtomicInteger(0)

        val thread = PathWatcher.watch(tempDir, 10, TimeUnit.MILLISECONDS) { changes ->
            println("changes: $changes")
            changeCounter.addAndGet(changes.size) > 10
        }

        val fileChanger = Executors.newSingleThreadExecutor().submit {
            var i = 0
            while (shouldCreateFiles.get()) {
                val run = i++
                println("run $run")
                System.out.flush()
                Files.write(
                    tempDir.resolve("test$run"),
                    "".toByteArray(Charsets.UTF_8),
                    StandardOpenOption.CREATE_NEW
                )
                Files.write(
                    tempDir.resolve("test" + run + "b"),
                    "".toByteArray(Charsets.UTF_8),
                    StandardOpenOption.CREATE_NEW
                )
                Thread.sleep(100)
            }
        }

        Thread.sleep(1000)
        thread.interrupt()

        shouldCreateFiles.set(false)
        fileChanger.get()
        println("DONE")
        System.out.flush()

    }

}