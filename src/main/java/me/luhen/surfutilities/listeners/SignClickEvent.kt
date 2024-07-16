package me.luhen.surfutilities.listeners

import me.luhen.surfutilities.Main
import me.luhen.surfutilities.utils.BuyInventory.BuyClaimInventory
import me.luhen.surfutilities.utils.JsonUtils
import me.ryanhamshire.GriefPrevention.GriefPrevention
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Sign
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import java.io.File

object SignClickEvent: Listener {

    val plugin = Main.instance

    @EventHandler
    fun signClickEvent(event: PlayerInteractEvent){

        if(event.action == Action.RIGHT_CLICK_BLOCK){

            if(event.clickedBlock?.type == Material.OAK_SIGN){

                val sign = event.clickedBlock!!.state as Sign

                if(plugin.curentClaim.containsKey(event.player)){

                    plugin.curentClaim.remove(event.player)

                }

                //Adds the player to a map to attach it to the claim being commercialized
                plugin.curentClaim[event.player] = sign.location

                if(sign.getLine(0).equals("Buy From", true)) {

                    if(sign.getLine(1) != event.player.name){

                        //Get the plugin data folder
                        val file = File(
                            Bukkit.getServer().pluginManager.getPlugin("SurfUtilities")!!.dataFolder,
                            "data/SignLocations.json")

                        val claims = JsonUtils.getClaimsFromJson(file)

                        val location = event.clickedBlock!!.location

                        val claimId = (GriefPrevention.instance.dataStore.getClaimAt(
                            location, true, true, null).id)

                        val existingClaim = claims.find { it.claimId == claimId }

                        if(existingClaim != null){

                            BuyClaimInventory(sign.getLine(3), sign.getLine(1))

                        }

                    }

                }

            }

        }

        //Cancel the sign edition

    }

}