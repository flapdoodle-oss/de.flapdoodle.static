package de.flapdoodle.statik.pipeline.publish

import de.flapdoodle.statik.files.FileSet
import de.flapdoodle.statik.pipeline.generate.ProcessedMediaFiles
import de.flapdoodle.statik.pipeline.generate.RendererPages
import de.flapdoodle.statik.types.Either
import io.undertow.Undertow
import io.undertow.server.HttpServerExchange
import io.undertow.util.Headers
import io.undertow.util.StatusCodes
import org.xnio.Option
import org.xnio.Options
import java.nio.ByteBuffer
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Singleton

@Singleton
class UndertowPublisher : Publisher {
    private val rendererPages: AtomicReference<RendererPages> = AtomicReference()
    private val mediaFiles: AtomicReference<ProcessedMediaFiles> = AtomicReference()

    private val server = Undertow.builder()
        .addHttpListener(8080, "localhost")
        .setHandler { exchange ->
            val requestPath = exchange.requestPath
            val response = responseFor(requestPath)

            exchange.statusCode=response.statusCode
            when (response.content) {
                is Either.Left -> {
                    exchange.responseHeaders.put(Headers.CONTENT_TYPE,response.mimeType+"; charset=UTF-8")
                    exchange.responseSender.send(response.content.left)
                }
                is Either.Right -> {
                    exchange.responseHeaders.put(Headers.CONTENT_TYPE,response.mimeType)
                    exchange.responseSender.send(ByteBuffer.wrap(response.content.right))
                }
            }

//            val pages = rendererPages.get()
//            if (pages != null) {
//                val page: RendererPages.Page? = pages.find(requestPath)
//                if (page!=null) {
//                    exchange.responseHeaders.put(
//                        Headers.CONTENT_TYPE,page.mimeType + "; charset=UTF-8"
//                    )
//                    exchange.responseSender.send(page.content)
//                } else {
//                    pageNotFound(exchange, pages)
//                }
//            } else {
//                exchange.statusCode = 404
//                exchange.responseHeaders.put(Headers.CONTENT_TYPE, "text/plain")
//                exchange.responseSender.send("no content at all")
//            }
        }
        .setServerOption(Options.THREAD_DAEMON,true)
        .build()

    init {
        server.start()
    }

    private fun responseFor(requestPath: String): Response {
        val pages = rendererPages.get()
        if (pages != null) {
            val page = pages.find(requestPath)
            if (page!=null) return Response(
                statusCode = StatusCodes.OK,
                content = Either.left(page.content),
                mimeType = page.mimeType
            )
        }
        val files = mediaFiles.get()
        if (files != null) {
            val file = files.find(requestPath)
            if (file!=null) return Response(
                statusCode = StatusCodes.OK,
                content = Either.right(file.content),
                mimeType = file.mimeType
            )
        }
        if (pages!=null || files!=null) {
            return Response(
                statusCode = StatusCodes.NOT_FOUND,
                content = Either.left(pageNotFoundMessage(pages, files)),
                mimeType = "text/html"
            )
        }
        return Response(
            statusCode = StatusCodes.NOT_FOUND,
            content = Either.left("no content at all"),
            mimeType = "text/plain"
        )
    }

    override fun baseUrl(): String {
        return ""
    }

    override fun publish(rendererPages: RendererPages, mediaFiles: ProcessedMediaFiles) {
        this.rendererPages.set(rendererPages)
        this.mediaFiles.set(mediaFiles)
    }

    fun stop() {
        server.stop()
    }

    companion object {
        private fun pageNotFoundMessage(
            rendererPages: RendererPages?,
            mediaFiles: ProcessedMediaFiles?
        ): String {
            val sb = StringBuilder()
            sb.append("<html><head><title>Page Not Found</title></head><body>")
            sb.append("<h1>Page Not Found</h1>")
            sb.append("<h2>Pages:</h2>")
            if (rendererPages!=null) {
                rendererPages.pages.forEach { page ->
                    sb.append("<a href=\"").append(page.path).append("\">").append(page.path).append("</a><br>\n")
                }
            }
            sb.append("<h2>Files:</h2>")
            if (mediaFiles!=null) {
                mediaFiles.files.forEach { file ->
                    sb.append("<a href=\"").append(file.path).append("\">").append(file.path).append("</a><br>\n")
                }
            }
            sb.append("</body></html>")
            return sb.toString()
        }
    }

    data class Response(
        val statusCode: Int,
        val content: Either<String, ByteArray>,
        val mimeType: String
    )
}