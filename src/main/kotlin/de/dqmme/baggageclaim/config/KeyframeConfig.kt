package de.dqmme.baggageclaim.config

import de.dqmme.baggageclaim.util.toBlockLocation
import org.bukkit.Location
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

object KeyframeConfig {
    private lateinit var instance: JavaPlugin
    private lateinit var yamlConfiguration: FileConfiguration
    private lateinit var file: File

    fun load(instance: JavaPlugin) {
        this.instance = instance

        file = File(instance.dataFolder, "keyframes.yml")

        if (!file.exists()) {
            instance.saveResource("keyframes.yml", false)
        }

        reload()
    }

    fun saveKeyframe(location: Location, id: Int? = null): Int {
        var newId = id

        if (newId == null) {
            newId = yamlConfiguration.getKeys(false).size
        }

        yamlConfiguration.set("$newId", location.subtract(0.0, 1.0, 0.0))
        return newId
    }

    private fun getKeyframe(id: Int): Pair<Int, Location?> {
        return id to yamlConfiguration.get("$id") as? Location
    }

    fun getKeyframes(): HashMap<Int, Location> {
        val hashMap = hashMapOf<Int, Location>()

        yamlConfiguration.getKeys(false).sortedBy { it }.forEach {
            val id = it.toIntOrNull() ?: return@forEach
            val location = getKeyframe(id).second ?: return@forEach

            hashMap[id] = location
        }

        return hashMap
    }

    fun getNextKeyframe(keyframe: Int = -1): Pair<Int, Location?>? {
        val highestKeyframe = getKeyframes().keys.sortedByDescending { it }.getOrNull(0) ?: return null

        if (keyframe >= highestKeyframe) return null

        val newKeyframe = getKeyframe(keyframe + 1)

        return if (newKeyframe.second != null) newKeyframe
        else getNextKeyframe(keyframe + 2)
    }

    fun getKeyframeByLocation(location: Location): Pair<Int, Location>? {
        getKeyframes().forEach {
            if (it.value.toBlockLocation() == location.toBlockLocation()) return it.key to it.value
        }

        return null
    }

    fun deleteKeyframe(id: Int) {
        yamlConfiguration.set("$id", null)
    }

    fun save() {
        yamlConfiguration.save(file)
    }

    private fun reload() {
        yamlConfiguration = YamlConfiguration.loadConfiguration(file)
    }
}