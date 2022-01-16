plugins {
    java
    id("maven-publish")
}
group = "org.kakara"
val artifactName = "engine"
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

subprojects {


    repositories {
        mavenLocal()
        maven("https://repo.maven.apache.org/maven2")
        maven("https://repo.kingtux.me/storages/maven/kakara")
    }
}
tasks.register<Javadoc>("aggregatedJavadocs") {
    setDestinationDir(file("$buildDir/docs/javadoc"))
    title = "$project.name $version API"
    options.withGroovyBuilder {
        "author"(true)
        "addStringOption"("Xdoclint:none", "-quiet")
        "addStringOption"("sourcepath", "")
    }
    subprojects.forEach { proj ->
        proj.tasks.filterIsInstance<Javadoc>().forEach {
            source += it.source
            classpath += it.classpath
            excludes += it.excludes
            includes += it.includes
        }
    }
}