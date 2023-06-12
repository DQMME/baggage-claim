package de.dqmme.baggageclaim.util

import de.dqmme.baggageclaim.config.KeyframeConfig
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.scheduler.BukkitRunnable

class ItemMovement(private val armorStand: ArmorStand, startKeyframe: Int = -1) : BukkitRunnable() {
    private var keyframe = startKeyframe

    override fun run() {
        teleport()
    }

    private fun teleport() {
        if (!armorStand.isValid) {
            armorStand.remove()
            cancel()
        }

        val nextKeyframe = KeyframeConfig.getNextKeyframe(keyframe)
            ?: if (keyframe == -1) {
                armorStand.remove()
                return
            } else {
                keyframe = -1
                teleport()
                return
            }

        keyframe = nextKeyframe.first

        val location = Location(
            nextKeyframe.second!!.world,
            nextKeyframe.second!!.x,
            nextKeyframe.second!!.y - 0.5,
            nextKeyframe.second!!.z,
            nextKeyframe.second!!.yaw,
            nextKeyframe.second!!.pitch,
        )

        armorStand.teleport(location)
    }
}