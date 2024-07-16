package me.luhen.surfutilities.utils

data class ClaimEntry(
    val claimId: Long,
    val location: LocationData,
    val value: Int
)

data class LocationData(
    val worldName: String,
    val x: Double,
    val y: Double,
    val z: Double
) {
    // Function to convert from Bukkit's Location to LocationData
    companion object {
        fun fromLocation(location: org.bukkit.Location): LocationData {
            return LocationData(
                worldName = location.world?.name ?: "unknown",
                x = location.x,
                y = location.y,
                z = location.z
            )
        }
    }
}
