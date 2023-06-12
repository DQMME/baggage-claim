package de.dqmme.baggageclaim.listener

import de.dqmme.baggageclaim.BaggageClaim
import de.dqmme.baggageclaim.config.Config
import de.dqmme.baggageclaim.config.KeyframeConfig
import de.dqmme.baggageclaim.util.ItemMovement
import de.dqmme.baggageclaim.util.toBlockLocation
import org.bukkit.Bukkit
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.util.EulerAngle

class InteractListener(private val instance: BaggageClaim) : Listener {
    private val armorStands = arrayListOf<ArmorStand>()

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val player = event.player

        if (event.hand != EquipmentSlot.HAND) return

        if (event.action != Action.RIGHT_CLICK_BLOCK) return

        if (!event.player.isSneaking) return

        if (player.inventory.itemInMainHand == null) return

        val block = event.clickedBlock?.location?.toBlockLocation() ?: return
        val keyframe = KeyframeConfig.getKeyframeByLocation(block) ?: return

        val armorStand =
            block.world.spawnEntity(keyframe.second.subtract(0.0, 0.5, 0.0), EntityType.ARMOR_STAND) as ArmorStand

        armorStand.isVisible = false
        armorStand.isSilent = true
        armorStand.setGravity(false)
        armorStand.isCollidable = false

        val item = ItemStack(player.inventory.itemInMainHand.clone().type)
        val itemMeta = item.itemMeta!!

        val names = Config.getNames()

        if(names.isEmpty()) {
            itemMeta.displayName = "No Names Configured"
        } else {
            itemMeta.displayName = Config.getNames().random()
        }

        item.itemMeta = itemMeta

        val newMainHand = player.inventory.itemInMainHand.clone()

        newMainHand.amount = newMainHand.amount - 1

        player.inventory.setItem(player.inventory.heldItemSlot, newMainHand)

        armorStand.helmet = item

        armorStand.headPose = EulerAngle(Math.toRadians(90.0), 0.0, 0.0)

        armorStands.add(armorStand)

        val speed = (20 / Config.getSpeed()).toLong()
        val task = ItemMovement(armorStand, keyframe.first).runTaskTimer(instance, speed, speed)

        instance.tasks[task] = armorStand
    }

    @EventHandler
    fun onInteractAtEntity(event: PlayerInteractAtEntityEvent) {
        if (event.rightClicked !is ArmorStand) return

        if (!armorStands.contains(event.rightClicked)) return

        event.isCancelled = true

        val inventory = Bukkit.createInventory(null, 27, "Gepäckausgabe")

        inventory.setItem(13, (event.rightClicked as ArmorStand).helmet)

        event.player.openInventory(inventory)
    }
}