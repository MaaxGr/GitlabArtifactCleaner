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
    implementation ("io.insert-koin:koin-core:3.1.2")

    // config
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation("com.charleskorn.kaml:kaml:0.26.0")

    // database
    implementation("mysql:mysql-connector-java:8.0.22")
    implementation("org.ktorm:ktorm-core:3.2.0")

    // gitlab
    implementation("org.gitlab4j:gitlab4j-api:4.15.7")

    // unit test
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.4.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")

}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    workingDir = File("${projectDir.absolutePath}/run")
}

tasks.register("prepareKotlinBuildScriptModel"){}

application {
    mainClass.set("gr.maax.gac.MainKt")
}

tasks.named<JavaExec>("run") {
    workingDir = File("${projectDir.absolutePath}/run")
}