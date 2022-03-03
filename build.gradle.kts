tasks {
    getByName<Wrapper>("wrapper") {
        gradleVersion = "7.2"
        distributionType = Wrapper.DistributionType.ALL
    }
}

defaultTasks(
        "clean", "build"
)

allprojects {
    repositories {
        mavenCentral()
    }
}
