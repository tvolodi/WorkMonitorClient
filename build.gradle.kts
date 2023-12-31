import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.9.20"

}

group = "org.vt-ptm"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
//    runtimeOnly("androidx.compose.runtime:runtime:1.5.4")
//    runtimeOnly("androidx.compose.runtime:runtime-livedata:1.5.4")
//    runtimeOnly("androidx.compose.runtime:runtime-rxjava2:1.5.4")
    implementation("org.jetbrains.compose.runtime:runtime-desktop:1.5.3")

    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
    implementation("com.arkivanov.decompose:decompose:2.1.4")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:2.1.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.1")
    implementation("commons-codec:commons-codec:1.16.0")
    implementation("io.ktor:ktor-client-auth:2.3.6")
    implementation("io.ktor:ktor-client-core:2.3.6")
    implementation("io.ktor:ktor-client-serialization:2.3.6")
    implementation("io.ktor:ktor-client-cio:2.3.6")
    implementation("io.ktor:ktor-client-json-jvm:2.3.6")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.6")
    implementation("io.ktor:ktor-client-resources:2.3.6")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.6")
    implementation("io.ktor:ktor-server-core:2.3.6")
    implementation("io.ktor:ktor-server-netty:2.3.6")
    implementation("com.auth0:java-jwt:4.4.0")

    implementation("org.xerial:sqlite-jdbc:3.44.0.0")
    implementation("org.slf4j:slf4j-api:1.7.36")

    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.20-1.0.14")



//    implementation(compose.material3-window-size-class)
//    implementation("androidx.compose.material3:material3-window-size-class:1.1.2")

}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "WorkMonitorClient"
            packageVersion = "1.0.0"
        }
    }
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
