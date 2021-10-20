plugins {
    application
    kotlin("jvm") version Versions.KOTLIN
    kotlin("plugin.serialization") version Versions.KOTLIN
}

group = "gr.maax"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    // ktor core
    implementation("io.ktor:ktor-server-core:1.6.4")
    implementation("io.ktor:ktor-server-netty:1.6.4")
    implementation("io.ktor:ktor-gson:1.6.4")

    implementation("ch.qos.logback:logback-classic:1.2.5")

    // dependency injection


    // config
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation("com.charleskorn.kaml:kaml:0.26.0")

    // database
    implementation("mysql:mysql-connector-java:8.0.22")
    implementation("org.ktorm:ktorm-core:3.2.0")

    // gitlab
    implementation("org.gitlab4j:gitlab4j-api:4.15.7")


    implementation ("io.insert-koin:koin-core:3.1.2")

}

tasks.register("prepareKotlinBuildScriptModel"){}

application {
    mainClass.set("gr.maax.gac.MainKt")
}

tasks.named<JavaExec>("run") {
    workingDir = File("${projectDir.absolutePath}/run")
}