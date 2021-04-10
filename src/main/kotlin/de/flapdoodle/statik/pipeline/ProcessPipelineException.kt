package de.flapdoodle.statik.pipeline

import java.lang.RuntimeException

class ProcessPipelineException(message: String, ex: Throwable) : RuntimeException(message, ex)