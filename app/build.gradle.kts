import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.owasp.dependencycheck.reporting.ReportGenerator.Format
import java.lang.System.getenv

plugins {
    application
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.allopen")
    id("com.github.johnrengelman.shadow")
    id("io.spring.dependency-management")
    id("au.com.dius.pact")
    id("org.owasp.dependencycheck")
}

val minorVersion: String = getenv().getOrDefault("CI_PIPELINE_ID", "")

group = "spy.bot"
version = if (minorVersion.isEmpty()) "DEV" else "0.$minorVersion.0"

// library and framework
val kotlinVersion: String by project
val kotlinxCoroutinesVersion: String by project
val micronautVersion: String by project
val jakartaInjectVersion: String by project

// implementations
val commonsCsvVersion: String by project
val googleBOMVersion: String by project
val jacksonVersion: String by project
val jsoupVersion: String by project
val resilience4jVersion: String by project
val uuidGeneratorVersion: String by project

// logging
val kotlinLoggingVersion: String by project
val julToSlf4jVersion: String by project
val logbackClassicVersion: String by project
val logbackEncoderVersion: String by project

// tests
val assertJVersion: String by project
val mockkVersion: String by project
val junitJupiterEngineVersion: String by project
val jsonAssertVersion: String by project
val micronautJunit: String by project
val pactVersion: String by project
val reactorTestVersion: String by project
val testContainersVersion: String by project
val wiremockVersion: String by project

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
    implementation("jakarta.inject:jakarta.inject-api:$jakartaInjectVersion")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:$jacksonVersion")

    implementation(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    implementation("io.micronaut:micronaut-http-server-netty")
    implementation("io.micronaut.security:micronaut-security")
    implementation("io.micronaut.micrometer:micronaut-micrometer-core")
    implementation("io.micronaut.micrometer:micronaut-micrometer-registry-prometheus")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.projectreactor:reactor-core")

    implementation(platform("com.google.cloud:libraries-bom:$googleBOMVersion"))
    implementation("com.google.cloud:google-cloud-datastore")
    implementation("com.google.cloud:google-cloud-storage")
    implementation("com.google.cloud:google-cloud-pubsub")

    implementation("org.apache.commons:commons-csv:$commonsCsvVersion")
    implementation("com.fasterxml.uuid:java-uuid-generator:$uuidGeneratorVersion")
    implementation("io.github.resilience4j:resilience4j-all:$resilience4jVersion")
    implementation("org.jsoup:jsoup:$jsoupVersion")

    implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
    implementation("org.slf4j:jul-to-slf4j:$julToSlf4jVersion")
    runtimeOnly("ch.qos.logback:logback-classic:$logbackClassicVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logbackEncoderVersion")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterEngineVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterEngineVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterEngineVersion")
    testImplementation("org.assertj:assertj-core:$assertJVersion")
    testImplementation("org.skyscreamer:jsonassert:$jsonAssertVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("org.testcontainers:testcontainers:$testContainersVersion")
    testImplementation("io.micronaut.test:micronaut-test-junit5:$micronautJunit")
    testImplementation("io.micronaut.test:micronaut-test-core:$micronautJunit")
    testImplementation("com.github.tomakehurst:wiremock-jre8:$wiremockVersion")
    testImplementation("au.com.dius.pact.consumer:java8:$pactVersion")
    testImplementation("com.google.cloud:google-cloud-nio")
    testImplementation("io.projectreactor:reactor-test:$reactorTestVersion")

    testImplementation("com.hierynomus:sshj:0.31.0")

    kapt(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    kapt("io.micronaut:micronaut-inject-java")
    kaptTest(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    kaptTest("io.micronaut:micronaut-inject-java")

}

tasks {
    named<ShadowJar>("shadowJar") {
        mergeServiceFiles()
    }

    named<JavaExec>("run") {
        jvmArgs(listOf("-noverify", "-XX:TieredStopAtLevel=1"))
    }

    named<KotlinCompile>("compileKotlin") {
        kotlinOptions {
            jvmTarget = "11"
            javaParameters = true
        }
    }

    named<KotlinCompile>("compileTestKotlin") {
        kotlinOptions {
            jvmTarget = "11"
            javaParameters = true
        }
    }

    withType<Test> {
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
    }

    named<Test>("test") {
        useJUnitPlatform()
    }

    register("jsonLogback") {
        doLast {
            project.copy {
                from(file("$rootDir/app/src/main/resources/logback-json.xml"))
                into(file("$rootDir/app/src/main/resources/"))
                rename { fileName: String ->
                    fileName.replace("logback-json.xml", "logback.xml")
                }
            }
        }
    }
}

java {
    sourceCompatibility = JavaVersion.toVersion("11")
}

application {
    // When shadowJar supports getting the main class name from new way in gradle, remove this line:
    mainClassName = "spy.bot.Application"

    // this is the correct way to specify, keeping both for shadowJar
    mainClass.set("spy.bot.Application")
}

allOpen {
    annotation("io.micronaut.aop.Around")
}

dependencyCheck {
    format = Format.ALL
    suppressionFile = "$rootDir/app/config/dependency-checker/suppressions.xml"
    failBuildOnCVSS = 0.00f
    if (!project.hasProperty("localDependencyCheck")) {
        cve.urlModified = "http://nist-nvd-mirror:8080/nvdcve-1.1-modified.json.gz"
        cve.urlBase = "http://nist-nvd-mirror:8080/nvdcve-1.1-%d.json.gz"
    } else {
        cve.urlModified = "https://nvd.nist.gov/feeds/json/cve/1.1/nvdcve-1.1-modified.json.gz"
        cve.urlBase = "https://nvd.nist.gov/feeds/json/cve/1.1/nvdcve-1.1-%d.json.gz"
    }
}
