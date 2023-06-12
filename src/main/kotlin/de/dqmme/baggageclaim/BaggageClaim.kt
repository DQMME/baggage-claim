package de.dqmme.baggageclaim

import de.dqmme.baggageclaim.command.KeyframeCommand
import de.dqmme.baggageclaim.config.Config
import de.dqmme.baggageclaim.config.KeyframeConfig
import de.dqmme.baggageclaim.listener.InteractListener
import de.dqmme.baggageclaim.listener.InventoryClickListener
import org.bukkit.entity.ArmorStand
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import java.util.UUID

class BaggageClaim : JavaPlugin() {
    val tasks = hashMapOf<BukkitTask, ArmorStand>()

    override fun onEnable() {
        Config.load(this)
        KeyframeConfig.load(this)
        server.pluginManager.registerEvents(InteractListener(this), this)
        server.pluginManager.registerEvents(InventoryClickListener(), this)

        getCommand("keyframe").executor = KeyframeCommand()
    }


    override fun onDisable() {
        Config.save()
        KeyframeConfig.save()

        tasks.forEach {
            it.key.cancel()
            it.value.remove()
        }
    }
}