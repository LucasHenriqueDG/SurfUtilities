package me.luhen.surfutilities.listeners

import me.luhen.surfutilities.Main
import me.luhen.surfutilities.utils.BuyInventory.BuyClaimInventory
import org.bukkit.Material
import org.bukkit.block.Sign
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

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

                plugin.curentClaim[event.player] = sign.location

                if(sign.getLine(0).equals("Buy From", true)) {


                    event.player.openInventory(BuyClaimInventory(sign.getLine(3), sign.getLine(1)))
                    event.isCancelled

                }
            }
        }

    }
}