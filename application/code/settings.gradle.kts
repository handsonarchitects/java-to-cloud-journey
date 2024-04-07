rootProject.name = "knotx-starter-kit"

pluginManagement {
    val knotxVersion: String by settings
    plugins {
        id("io.knotx.distribution") version knotxVersion
        id("io.knotx.release-base") version knotxVersion
        id("com.bmuschko.docker-remote-api") version "9.4.0"
        id("org.nosphere.apache.rat") version "0.6.0"
    }
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

include("example-api")
include("health-check")
include("example-action")

project(":example-api").projectDir = file("modules/example-api")
project(":health-check").projectDir = file("modules/health-check")
project(":example-action").projectDir = file("modules/example-action")
