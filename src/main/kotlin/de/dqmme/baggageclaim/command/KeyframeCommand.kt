package de.dqmme.baggageclaim.command

import de.dqmme.baggageclaim.config.KeyframeConfig
import de.dqmme.baggageclaim.util.DefaultMessages
import de.dqmme.baggageclaim.util.PREFIX
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class KeyframeCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (!sender.hasPermission("bc.keyframe") && !sender.hasPermission("bc.*")) {
            sender.sendMessage(DefaultMessages.NO_PERMISSION)
            return false
        }

        if (args.isEmpty()) {
            sender.sendMessage(DefaultMessages.INVALID_USAGE.replace("%USAGE%", "/keyframe <add/remove/list>"))
            return false
        }

        if (sender !is Player) {
            sender.sendMessage(DefaultMessages.NOT_A_PLAYER)
            return false
        }

        when (args[0]) {
            "add" -> {
                val id: Int?

                if (args.size == 1) {
                    id = KeyframeConfig.saveKeyframe(sender.location)
                } else {
                    id = args[1].toIntOrNull()

                    if (id == null || id < 0) {
                        sender.sendMessage(DefaultMessages.INVALID_USAGE.replace("%USAGE%", "/keyframe add (id)"))
                        return false
                    }

                    KeyframeConfig.saveKeyframe(sender.location, id)
                }

                sender.sendMessage("$PREFIX §aDu hast erfolgreich einen Keyframe gesetzt. §bID: §6$id")
            }

            "remove" -> {
                if (args.size != 2) {
                    sender.sendMessage(DefaultMessages.INVALID_USAGE.replace("%USAGE%", "/keyframe remove <id>"))
                    return false
                }

                val id = args[1].toIntOrNull()

                if (id == null) {
                    sender.sendMessage(DefaultMessages.INVALID_USAGE.replace("%USAGE%", "/keyframe remove <id>"))
                    return false
                }

                KeyframeConfig.deleteKeyframe(id)
                sender.sendMessage("$PREFIX §aDu hast den Keyframe §6$id §aerfolgreich gelöscht.")
            }

            "list" -> {
                val keyframes = KeyframeConfig.getKeyframes()
                var message = "$PREFIX - §aKeyframes:"

                if (keyframes.isEmpty()) {
                    message += "\n§cDu hast noch keine Keyframes gesetzt."
                    sender.sendMessage(message)
                    return true
                }

                keyframes.forEach {
                    fun Location.format(): String {
                        return "$blockX, $blockY, $blockZ"
                    }

                    message += "\n§6${it.key} - §b${it.value.format()}"
                }

                sender.sendMessage(message)
            }

            else -> {
                sender.sendMessage(DefaultMessages.INVALID_USAGE.replace("%USAGE%", "/keyframe <add/remove/list>"))
                return false
            }
        }

        return true
    }
}