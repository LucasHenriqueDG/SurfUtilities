package me.luhen.surfutilities.listeners

import me.luhen.surfutilities.utils.JsonUtils
import me.ryanhamshire.GriefPrevention.GriefPrevention
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import java.io.File

object SignBreakListener : Listener {
    //Used to remove the claim sale when the sign is broken

    private val jsonFile: File = File(Bukkit.getServer().pluginManager.getPlugin("SurfUtilities")!!.dataFolder,
        "data/SignLocations.json")

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val block = event.block

        // Check if the block is a sign
        if (block.type == Material.OAK_SIGN) {

            val location = block.location

            val player = event.player

            val claimId = (GriefPrevention.instance.dataStore.getClaimAt(
                location, true, true, null).id)

            val claimEntries = JsonUtils.getClaimsFromJson(jsonFile)

            val existingClaim = claimEntries.find { it.claimId == claimId }

            if (existingClaim != null) {

                if(GriefPrevention.instance.dataStore.getClaim(existingClaim.claimId).ownerName != player.name){

                    player.sendMessage("You do not have permission to break this sign.")

                    event.isCancelled = true

                } else {

                    //Remove the claim from the json file
                    JsonUtils.removeClaimEntryById(jsonFile, existingClaim.claimId)

                }

            }

        }

    }

}
