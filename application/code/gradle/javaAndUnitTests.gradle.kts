import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

allprojects {
    plugins.withId("java") {
        tasks.withType<JavaCompile>().configureEach {
            with(options) {
                compilerArgs = listOf("-parameters")
                encoding = "UTF-8"
            }
        }

        tasks.withType<Test>().configureEach {
            systemProperties(Pair("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.SLF4JLogDelegateFactory"))

            failFast = true
            useJUnitPlatform()
            testLogging {
                events = setOf(TestLogEvent.FAILED)
                exceptionFormat = TestExceptionFormat.SHORT
            }

            dependencies {
                "testImplementation"(platform("io.knotx:knotx-dependencies:${project.property("knotxVersion")}"))
                "testImplementation"("org.junit.jupiter:junit-jupiter-api")
                "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine")
                "testImplementation"(group = "io.rest-assured", name = "rest-assured", version = "5.4.0")
            }
        }

        tasks.withType<Test>().configureEach {
            include("**/*Test*")
        }
    }
}