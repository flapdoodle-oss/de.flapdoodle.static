package de.flapdoodle.statik.pipeline.publish

import de.flapdoodle.statik.pipeline.generate.RendererPages
import io.undertow.Undertow
import io.undertow.server.HttpServerExchange
import io.undertow.util.Headers
import org.xnio.Option
import org.xnio.Options
import java.util.concurrent.atomic.AtomicReference

class UndertowPublisher : Publisher {
    private val rendererPages: AtomicReference<RendererPages> = AtomicReference()

    private val server = Undertow.builder()
        .addHttpListener(8080, "localhost")
        .setHandler { exchange ->
            val requestPath = exchange.requestPath
            val pages = rendererPages.get()
            if (pages != null) {
                val page: RendererPages.Page? = pages.find(requestPath)
                if (page!=null) {
                    exchange.responseHeaders.put(
                        Headers.CONTENT_TYPE,page.mimeType + "; charset=UTF-8"
                    )
                    exchange.responseSender.send(page.content)
                } else {
                    pageNotFound(exchange, pages)
                }
            } else {
                exchange.statusCode = 404
                exchange.responseHeaders.put(Headers.CONTENT_TYPE, "text/plain")
                exchange.responseSender.send("no content at all")
            }
        }
        .setServerOption(Options.THREAD_DAEMON,true)
        .build()

    init {
        server.start()
    }

    override fun baseUrl(): String {
        return ""
    }

    override fun publish(rendererPages: RendererPages) {
        this.rendererPages.set(rendererPages)
    }

    fun stop() {
        server.stop()
    }

    companion object {
        private fun pageNotFound(exchange: HttpServerExchange, rendererPages: RendererPages) {
            exchange.statusCode = 404
            exchange.responseHeaders.put(Headers.CONTENT_TYPE, "text/html")
            exchange.responseSender.send(pageNotFoundMessage(rendererPages))
        }

        private fun pageNotFoundMessage(rendererPages: RendererPages): String? {
            val sb = StringBuilder()
            sb.append("<html><head><title>Page Not Found</title></head><body>")
            sb.append("<h1>Page Not Found</h1>")
            rendererPages.pages.forEach { page ->
                sb.append("<a href=\"").append(page.path).append("\">").append(page.path).append("</a><br>\n")
            }
            sb.append("</body></html>")
            return sb.toString()
        }
    }
}