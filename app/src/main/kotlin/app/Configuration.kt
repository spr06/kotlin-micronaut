package app

import io.micronaut.context.annotation.ConfigurationProperties
import jakarta.inject.Inject

@ConfigurationProperties("spy-bot")
class Configuration {
    lateinit var projectId: String
}