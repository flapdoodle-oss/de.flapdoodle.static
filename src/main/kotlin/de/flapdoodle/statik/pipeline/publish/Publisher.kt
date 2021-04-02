package de.flapdoodle.statik.pipeline.publish

import de.flapdoodle.statik.pipeline.generate.RendererPages

interface Publisher {
    fun publish(rendererPages: RendererPages)
    fun baseUrl(): String
}