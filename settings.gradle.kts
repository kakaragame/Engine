rootProject.name = "engine"

pluginManagement {
    repositories {
        maven("https://repo.kingtux.me/storages/maven/kingtux-repo")
        gradlePluginPortal()
    }
}

object Versions {
    val kakara = "1.0-SNAPSHOT"
}