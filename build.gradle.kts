import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    antlr
    application
}

group = "io.github.kylemclean.scriptengine"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    antlr("org.antlr:antlr4:4.9.3")
    implementation("org.jline:jline:3.21.0")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "17"
    dependsOn(tasks.generateGrammarSource)
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes["Main-Class"] = "io.github.kylemclean.scriptengine.MainKt"
    }

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

tasks.generateGrammarSource {
    arguments = arguments + listOf("-visitor")
}
