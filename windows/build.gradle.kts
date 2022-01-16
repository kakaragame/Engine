import org.gradle.internal.os.OperatingSystem

plugins {
    java
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.kakara.versionfile") version "1.0.0"
    `maven-publish`
    signing
}

val lwjglVersion = "3.2.3"
val jomlVersion = "1.9.25"

// LWJGL Native Versions
var lwjglNatives = when (OperatingSystem.current()) {
    OperatingSystem.LINUX -> System.getProperty("os.arch").let {
        if (it.startsWith("arm") || it.startsWith("aarch64"))
            "natives-linux-${if (it.contains("64") || it.startsWith("armv8")) "arm64" else "arm32"}"
        else
            "natives-linux"
    }
    OperatingSystem.WINDOWS -> if (System.getProperty("os.arch")
            .contains("64")
    ) "natives-windows" else "natives-windows-x86"
    OperatingSystem.MAC_OS -> "natives-macos"
    else -> throw Error("Unrecognized or unsupported Operating system. Please set \"lwjglNatives\" manually")
}

if (hasProperty("native")) {
    lwjglNatives = properties.get("native").toString()
}

group = "org.kakara"
val artifactName = "engine-windows"
var build = "0"
var branch = ""

if (hasProperty("branch")) {
    branch = properties.get("branch").toString()
}

if (hasProperty("buildNumber")) {
    version = org.kakara.engine.Version.getEngineVersion(properties.get("buildNumber").toString(), branch);
} else {
    version = org.kakara.engine.Version.getEngineVersion("", branch);
}

java {
    targetCompatibility = JavaVersion.VERSION_17
    sourceCompatibility = JavaVersion.VERSION_17
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
    maven("https://repo.ryandw11.com/repository/maven-releases")
    jcenter()
}

publishing {
    publications {

        create<MavenPublication>("mavenJava") {
            artifact(tasks["shadowJar"])

            artifactId = artifactName
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name.set(artifactName)
            }
        }
    }
    repositories {
        maven {

            val releasesRepoUrl = uri("https://repo.kingtux.me/storages/maven/kakara")
            val snapshotsRepoUrl = uri("https://repo.kingtux.me/storages/maven/kakara")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            credentials(PasswordCredentials::class)

        }
        mavenLocal()
    }
}

