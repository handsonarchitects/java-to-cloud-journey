plugins {
    id("io.knotx.distribution")
    id("io.knotx.release-base")
    id("com.bmuschko.docker-remote-api")
    id("java")
}

dependencies {
    subprojects.forEach { "dist"(project(":${it.name}")) }
}

sourceSets.named("test") {
    java.srcDir("functional/src/test/java")
}

allprojects {
    group = "com.project"

    repositories {
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("https://oss.sonatype.org/content/groups/staging/") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    }

    pluginManager.withPlugin("java") {
        java {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(11))
                vendor.set(JvmVendorSpec.ADOPTIUM)
            }
        }
    }
}

tasks.named("build") {
    dependsOn("build-stack")
}

tasks.register("build-docker") {
    group = "docker"
    dependsOn("runFunctionalTest")
}

tasks.register("build-stack") {
    group = "stack"
    // https://github.com/Knotx/knotx-gradle-plugins/blob/master/src/main/kotlin/io/knotx/distribution.gradle.kts
    dependsOn("assembleCustomDistribution")
    mustRunAfter("build-docker")
}

/** Knot.x releasing, can be removed **/

tasks {
    named<io.knotx.release.common.ProjectVersionUpdateTask>("setVersion") {
        group = "release prepare"
        versionParamProperty = "knotxVersion"
        propertyKeyNameInFile = "knotxVersion"
    }

    named("build-docker") {
        mustRunAfter("setVersion")
    }

    named("updateChangelog") {
        dependsOn("build-docker", "setVersion")
    }

    register("prepare") {
        group = "release"
        dependsOn("updateChangelog")
    }

    register("publishArtifacts") {
        group = "release"
        logger.lifecycle("Starter-kit does not publish anything")
    }
}

apply(from = "gradle/javaAndUnitTests.gradle.kts")
apply(from = "gradle/docker.gradle.kts")

