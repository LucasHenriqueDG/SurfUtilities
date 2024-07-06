package me.luhen.surfutilities.listeners

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import me.luhen.surfutilities.Main
import me.luhen.surfutilities.utils.SignLocation
import me.ryanhamshire.GriefPrevention.GriefPrevention
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileReader
import java.io.FileWriter

object SignBreakEvent : Listener {

    val plugin = Main.instance

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val block = event.block

        // Check if the block is a sign
        if (isSign(block)) {
            val player = event.player
            val location = block.location

            val jsonFile = File(plugin.dataFolder, "signLocations.json")

            // Load existing data from JSON file
            val existingData: MutableList<SignLocation> = if (jsonFile.exists()) {
                val reader = FileReader(jsonFile)
                Gson().fromJson(reader, object : TypeToken<MutableList<SignLocation>>() {}.type)
                    ?: mutableListOf()
            } else {
                mutableListOf()
            }

            // Find the matching sign entry
            val signEntry = existingData.find {
                it.matchesLocation(location) && it.playerName == player.name
            }

            if (signEntry != null) {
                // Remove the sign entry if found
                existingData.remove(signEntry)

                // Write updated data back to the JSON file
                val writer = FileWriter(jsonFile)
                Gson().toJson(existingData, writer)
                writer.flush()
                writer.close()

                player.sendMessage("You've cancelled this sale.")
            } else {
                // Cancel the event if the player is not the owner of the sign
                event.isCancelled = true
                player.sendMessage("Only the claim owner can break this sign.")
            }
        }
    }

    private fun isSign(block: Block): Boolean {
        return block.type == Material.OAK_SIGN
    }
}
