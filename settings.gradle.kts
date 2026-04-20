pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        // Using the explicit URL for Maven Central to bypass potential routing issues
        maven { url = uri("https://repo1.maven.org/maven2/") }
        mavenCentral()
    }
}

rootProject.name = "Split Wise"
include(":app")
