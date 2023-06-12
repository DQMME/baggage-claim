plugins {
    kotlin("jvm") version "1.8.10"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "de.dqmme"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
}

dependencies {
    //STDLib
    implementation(kotlin("stdlib"))

    // Spigot Dependency
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
}

bukkit {
    name = "BaggageClaim"
    apiVersion = "1.12"
    authors = listOf(
        "DQMME",
    )
    main = "$group.baggageclaim.BaggageClaim"
    version = getVersion().toString()

    commands {
        register("keyframe") {
            description = "Setup Command"
            permission = "bc.keyframe"
        }
    }
}