package de.dqmme.baggageclaim.config

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

object Config {
    private lateinit var instance: JavaPlugin
    private lateinit var yamlConfiguration: FileConfiguration

    fun load(instance: JavaPlugin) {
        this.instance = instance

        if (!File(instance.dataFolder, "config.yml").exists()) {
            instance.saveResource("config.yml", false)
        }

        reload()
        yamlConfiguration = instance.config
    }

    fun getSpeed() = yamlConfiguration.getInt("speed")

    fun getNames() = yamlConfiguration.getStringList("names")

    fun save() {
        instance.saveConfig()
    }

    private fun reload() {
        instance.reloadConfig()
    }
}