package de.dqmme.baggageclaim.util

import org.bukkit.Location

fun Location.toBlockLocation(): Location {
    return Location(world, blockX.toDouble(), blockY.toDouble(), blockZ.toDouble())
}