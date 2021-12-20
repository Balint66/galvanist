import org.gradle.kotlin.dsl.accessors.runtime.externalModuleDependencyFor

// Add External Plugins
pluginManagement {
    // Add MinecraftForge maven to plugin management
    repositories {
        maven { url = uri("https://maven.minecraftforge.net") }
        maven { url = uri("https://maven.parchmentmc.org")}
        mavenCentral()
    }

    // Resolve ForgeGradle within plugin block
    resolutionStrategy.eachPlugin {
        if (requested.id.id == "net.minecraftforge.gradle")
            useModule(buildscript.dependencies.create(group = requested.id.id, name = "ForgeGradle", version = requested.version))
        else if (requested.id.namespace == "org.parchmentmc.librarian.forgegradle")
            useModule(buildscript.dependencies.create(group = "org.parchmentmc", name = "librarian", version = requested.version))
    }
}
