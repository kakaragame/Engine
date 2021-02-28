import org.gradle.internal.os.OperatingSystem

plugins {
    java
    `java-library`
    id("com.github.johnrengelman.shadow") version "6.1.0"
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
val artifactName = "engine"
var build = "0"
var branch = "";
if (hasProperty("branch")) {
    branch = properties.get("branch").toString()
}
if (hasProperty("buildNumber")) {
    version = org.kakara.engine.Version.getEngineVersion(properties.get("buildNumber").toString(), branch);
} else {
    version = org.kakara.engine.Version.getEngineVersion("", branch);
}
java {
    targetCompatibility = JavaVersion.VERSION_11
    sourceCompatibility = JavaVersion.VERSION_11
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

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}

tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        if (project.hasProperty("is-build")) {
            //Used by Jenkins
            archiveFileName.set("${project.name}-${project.version}-${project.property("native")}.jar")
        }
        archiveClassifier.set("");
        dependsOn(project.tasks.getByName("vftask"));
    }
    "jar"{
        dependsOn(project.tasks.getByName("vftask"));
    }
}

versionFileConfig {
    isCompileIntoJar = true;
    jarDirectory = "engine"
}
dependencies {
    // Regular Depends
    implementation(group = "me.ryandw11", name = "Octree", version = "1.0")
    implementation("org.apache.commons:commons-lang3:3.11")
    implementation("commons-io:commons-io:2.8.0")
    implementation("org.jetbrains:annotations:20.1.0")
    implementation("com.github.nifty-gui:nifty:1.4.3")
    implementation("org.l33tlabs.twl:pngdecoder:1.0")
    implementation("io.imgui.java:binding:1.77-0.17.2")
    implementation("io.imgui.java:lwjgl3:1.77-0.17.2")
    implementation("org.slf4j:slf4j-api:2.0.0-alpha1")
    testRuntimeOnly("org.slf4j:slf4j-simple:2.0.0-alpha1")
    implementation("org.joml", "joml", jomlVersion)

    //LWJGL
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-assimp")
    implementation("org.lwjgl", "lwjgl-bgfx")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-nanovg")
    implementation("org.lwjgl", "lwjgl-openal")
    implementation("org.lwjgl", "lwjgl-opencl")
    implementation("org.lwjgl", "lwjgl-opengl")
    implementation("org.lwjgl", "lwjgl-par")
    implementation("org.lwjgl", "lwjgl-stb")
    implementation("org.lwjgl", "lwjgl-vulkan")
    if (!lwjglNatives.equals("build", true)) {
        runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
        runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = lwjglNatives)
        runtimeOnly("org.lwjgl", "lwjgl-bgfx", classifier = lwjglNatives)
        runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
        runtimeOnly("org.lwjgl", "lwjgl-nanovg", classifier = lwjglNatives)
        runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = lwjglNatives)
        runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
        runtimeOnly("org.lwjgl", "lwjgl-par", classifier = lwjglNatives)
        runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)
        implementation("io.imgui.java:$lwjglNatives:1.77-0.17.2")
    }

    if (lwjglNatives == "natives-macos") runtimeOnly("org.lwjgl", "lwjgl-vulkan", classifier = lwjglNatives)
}
