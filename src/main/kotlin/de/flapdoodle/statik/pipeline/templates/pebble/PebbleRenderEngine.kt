package de.flapdoodle.statik.pipeline.templates.pebble

import com.mitchellbosecke.pebble.PebbleEngine
import com.mitchellbosecke.pebble.loader.FileLoader
import de.flapdoodle.statik.pipeline.templates.RenderEngine
import de.flapdoodle.statik.pipeline.templates.Renderable
import java.io.StringWriter
import java.nio.file.Path

class PebbleRenderEngine(templatePath: Path) : RenderEngine {
    private val loader = FileLoader()
    private val engine = PebbleEngine.Builder()
        .loader(loader)
        .autoEscaping(false)
        //.extension()
        .build()

    init {
        loader.prefix = templatePath.toAbsolutePath().toString()
    }

    override fun render(templatePath: String, renderable: Renderable): String {
        val template = engine.getTemplate(templatePath+".html")
        
        val writer = StringWriter()

        template.evaluate(writer, mutableMapOf("it" to (PebbleWrapper(renderable) as Any)))

        return writer.toString()
    }

//    class CustomFileLoader(val templatePath: Path) : Loader<String> {
//        private val logger = LoggerFactory.getLogger(FileLoader::class.java)
//
//        private var prefix: String? = null
//        private var suffix: String? = null
//        private var charset = "UTF-8"
//
//        override fun getReader(templateName: String): Reader? {
//            var `is`: InputStream? = null
//            val file = getFile(templateName)
//            if (file.exists() && file.isFile) {
//                try {
//                    `is` = FileInputStream(file)
//                } catch (var6: FileNotFoundException) {
//                }
//            }
//            return if (`is` == null) {
//                throw LoaderException(null as Throwable?, "Could not find template \"$templateName\"")
//            } else {
//                try {
//                    BufferedReader(InputStreamReader(`is`, charset))
//                } catch (var5: UnsupportedEncodingException) {
//                    null
//                }
//            }
//        }
//
//        private fun getFile(templateName: String): File {
//            var templateName = templateName
//            val path = StringBuilder()
//            if (getPrefix() != null) {
//                path.append(getPrefix())
//                if (!getPrefix()!!.endsWith(File.separatorChar.toString())) {
//                    path.append(File.separatorChar)
//                }
//            }
//            templateName = templateName + if (getSuffix() == null) "" else getSuffix()
//            logger.info("Looking for template in {}{}.", path.toString(), templateName)
//            val pathSegments = PathUtils.PATH_SEPARATOR_REGEX.split(templateName)
//            if (pathSegments.size > 1) {
//                templateName = pathSegments[pathSegments.size - 1]
//            }
//            for (i in 0 until pathSegments.size - 1) {
//                path.append(pathSegments[i]).append(File.separatorChar)
//            }
//            return File(path.toString(), templateName)
//        }
//
//        fun getSuffix(): String? {
//            return suffix
//        }
//
//        override fun setSuffix(suffix: String?) {
//            this.suffix = suffix
//        }
//
//        fun getPrefix(): String? {
//            return prefix
//        }
//
//        override fun setPrefix(prefix: String?) {
//            this.prefix = prefix
//        }
//
//        fun getCharset(): String? {
//            return charset
//        }
//
//        override fun setCharset(charset: String) {
//            this.charset = charset
//        }
//
//        override fun resolveRelativePath(relativePath: String?, anchorPath: String?): String? {
//            return PathUtils.resolveRelativePath(relativePath, anchorPath, File.separatorChar)
//        }
//
//        override fun createCacheKey(templateName: String?): String? {
//            return templateName
//        }
//
//        override fun resourceExists(templateName: String): Boolean {
//            return getFile(templateName).exists()
//        }
//
//    }
}