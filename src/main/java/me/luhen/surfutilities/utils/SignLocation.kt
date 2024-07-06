package me.luhen.surfutilities.utils

import org.bukkit.Location

data class SignLocation(val playerName: String,
                        val worldName: String,
                        val x: Double,
                        val y: Double,
                        val z: Double) {

    // Function to compare this SignLocation with another Location
    fun matchesLocation(location: Location): Boolean {
        return worldName == location.world?.name &&
                x == location.x &&
                y == location.y &&
                z == location.z
    }
}