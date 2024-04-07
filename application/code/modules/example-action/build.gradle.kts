plugins {
    `java-library`
}

dependencies {

    "io.knotx:knotx".let { v ->
        implementation(platform("$v-dependencies:${project.property("knotxVersion")}"))
        api("$v-fragments-api:${project.property("knotxVersion")}")
        api("$v-fragments-action-api:${project.property("knotxVersion")}")
    }
    "io.vertx:vertx".let { v ->
        implementation("$v-web")
        implementation("$v-web-client")
        implementation("$v-rx-java2")
    }
}
