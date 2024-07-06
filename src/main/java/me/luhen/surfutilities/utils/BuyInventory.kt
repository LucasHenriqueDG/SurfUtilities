package me.luhen.surfutilities.utils

import me.luhen.surfutilities.Main
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object BuyInventory {

    val plugin = Main.instance

    fun BuyClaimInventory(price: String, seller: String): Inventory {
        // Create an inventory with 9 slots
        val inventory = Bukkit.createInventory(null, 9, "Buy Claim")

        // Create items
        val signItem = ItemStack(Material.OAK_SIGN)
        val signMeta = signItem.itemMeta
        signMeta?.setDisplayName(plugin.config.getString("middle-item-text"))
        val signMetaLore = mutableListOf<String>()
        signMetaLore.add("§2Seller: §f${seller}")
        signMetaLore.add("§2Price: §f${price}")
        signMeta?.lore = signMetaLore
        signItem.setItemMeta(signMeta)

        val redWoolItem = ItemStack(Material.RED_WOOL)
        val redWoolMeta = redWoolItem.itemMeta
        redWoolMeta?.setDisplayName(plugin.config.getString("cancel-item-text"))
        redWoolItem.setItemMeta(redWoolMeta)


        val greenWoolItem = ItemStack(Material.GREEN_WOOL)
        val greenWoolMeta = greenWoolItem.itemMeta
        greenWoolMeta?.setDisplayName(plugin.config.getString("buy-item-text")?.replace("%price%", price))
        greenWoolItem.setItemMeta(greenWoolMeta)

        // Set items in specific slots
        inventory.setItem(4, signItem) // Middle slot
        inventory.setItem(3, redWoolItem) // Left of the sign
        inventory.setItem(5, greenWoolItem) // Right of the sign

        return inventory
    }

}