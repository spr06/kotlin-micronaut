package app

import io.micronaut.runtime.Micronaut
import mu.KLogging
import mu.KotlinLogging
import org.slf4j.bridge.SLF4JBridgeHandler

class Application {
    companion object : KLogging() {
        @JvmStatic
        fun main(args: Array<String>) {
            SLF4JBridgeHandler.removeHandlersForRootLogger()
            SLF4JBridgeHandler.install()
            Micronaut.build()
                .classes(Application::class.java)
                .banner(false)
                .start()
        }
    }
}

private val logger = KotlinLogging.logger {  }

