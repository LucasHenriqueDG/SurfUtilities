package me.luhen.griefpreventioneconomy.listeners

import me.luhen.griefpreventioneconomy.Main
import me.luhen.griefpreventioneconomy.utils.SellClaimCheck.SellClaimCheckBuyer
import me.ryanhamshire.GriefPrevention.GriefPrevention
import org.bukkit.Material
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

object OnInventoryClick: Listener {

    val plugin = Main.instance

        @EventHandler
        fun onInventoryClick(event: InventoryClickEvent) {
            // Check if the clicked inventory is our custom inventory
            if (event.view.title == "Buy Claim") {
                event.isCancelled = true // Cancel the click event

                if(event.currentItem?.type == Material.GREEN_WOOL){
                    val buyer = event.whoClicked as Player
                    val location = plugin.curentClaim.get(buyer)
                    val price = (plugin.curentClaim[event.whoClicked]?.block?.state as Sign).getLine(3)
                    SellClaimCheckBuyer(buyer, GriefPrevention.instance.dataStore.getClaimAt(
                        location, true, true, null), price.toInt())
                }
             }

            event.whoClicked.closeInventory()
        }
}