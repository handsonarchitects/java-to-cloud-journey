import com.bmuschko.gradle.docker.tasks.container.DockerCreateContainer
import com.bmuschko.gradle.docker.tasks.container.DockerStartContainer
import com.bmuschko.gradle.docker.tasks.container.DockerStopContainer
import com.bmuschko.gradle.docker.tasks.container.extras.DockerWaitHealthyContainer
import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerRemoveImage
import java.time.Duration

val dockerImageRef = "$buildDir/.docker/buildImage-imageId.txt"

tasks.register<Copy>("copyDockerfile") {
    group = "docker"
    from("docker")
    into("$buildDir")
    expand("knotx_version" to project.property("knotxVersion"))
    mustRunAfter("cleanDistribution")
}

tasks.register<DockerRemoveImage>("removeImage") {
    group = "docker"
    val spec = object : Spec<Task> {
        override fun isSatisfiedBy(task: Task): Boolean {
            return File(dockerImageRef).exists()
        }
    }
    onlyIf(spec)

    targetImageId(if (File(dockerImageRef).exists()) File(dockerImageRef).readText() else "")
    onError {
        if (!this.message!!.contains("No such image"))
            throw this
    }
}

tasks.register<DockerBuildImage> ("buildImage") {
    group = "docker"
    inputDir.set(file("$buildDir"))
    images.add("${project.property("docker.image.name")}:latest")
    dependsOn("removeImage", "prepareDocker")
}
val buildImage = tasks.named<DockerBuildImage>("buildImage")

// FUNCTIONAL TESTS

tasks.register<DockerCreateContainer>("createContainer") {
    group = "docker-functional-tests"
    dependsOn(buildImage)
    targetImageId(buildImage.get().imageId)
    hostConfig.portBindings.set(listOf("8092:8092"))
    hostConfig.autoRemove.set(true)
    exposePorts("tcp", listOf(8092))
}
val createContainer = tasks.named<DockerCreateContainer>("createContainer")

tasks.register<DockerStartContainer>("startContainer") {
    group = "docker-functional-tests"
    dependsOn(createContainer)
    targetContainerId(createContainer.get().containerId)
}

tasks.register<DockerWaitHealthyContainer>("waitContainer") {
    group = "docker-functional-tests"
    dependsOn(tasks.named("startContainer"))
    targetContainerId(createContainer.get().containerId)
    awaitStatusTimeout.set(60) // in seconds
}

tasks.register<DockerStopContainer>("stopContainer") {
    group = "docker-functional-tests"
    targetContainerId(createContainer.get().containerId)
}

tasks.register("runFunctionalTest", Test::class) {
    group = "docker-functional-tests"
    dependsOn(tasks.named("waitContainer"))
    finalizedBy(tasks.named("stopContainer"))
    include("**/*ITCase*")
}

tasks.register("prepareDocker") {
    dependsOn("cleanDistribution", "overwriteCustomFiles", "copyDockerfile")
}