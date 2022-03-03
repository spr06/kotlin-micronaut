rootProject.name = "moj-test"

include(
    "app"
)

pluginManagement {
    // DO NOT TRY TO USE import OR REMOVE THE PluginManagementSpec PREFIX - THESE ARE ALL NEEDED AS THE pluginManagement BLOCK DOES NOT
    // SEE OR ALLOW EXTERNAL REFERENCES WHEN IT COMPILES.
    fun PluginManagementSpec.loadProperties(fileName: String, path: String = rootDir.absolutePath) = java.util.Properties().also { properties ->
        File("$path/$fileName").inputStream().use {
            properties.load(it)
        }
    }
    val versions: java.util.Properties = loadProperties("gradle.properties")

    val kotlinVersion: String by versions
    val springDependencyManagementVersion: String by versions
    val shadowVersion: String by versions
    val pactPluginVersion: String by versions
    val owaspDependencyCheckVersion: String by versions

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.jvm" -> useVersion(kotlinVersion)
                "org.jetbrains.kotlin.kapt" -> useVersion(kotlinVersion)
                "org.jetbrains.kotlin.plugin.allopen" -> useVersion(kotlinVersion)
                "org.jetbrains.kotlin.plugin.serialization" -> useVersion(kotlinVersion)
                "io.spring.dependency-management" -> useVersion(springDependencyManagementVersion)
                "com.github.johnrengelman.shadow" -> useVersion(shadowVersion)
                "au.com.dius.pact" -> useVersion(pactPluginVersion)
                "org.owasp.dependencycheck" -> useVersion(owaspDependencyCheckVersion)
            }
        }
    }
}
