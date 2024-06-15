package me.luhen.griefpreventioneconomy.utils

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object BuyInventory {

    fun BuyClaimInventory(price: String, seller: String): Inventory {
        // Create an inventory with 9 slots
        val inventory = Bukkit.createInventory(null, 9, "Buy Claim")

        // Create items
        val signItem = ItemStack(Material.OAK_SIGN)
        val signMeta = signItem.itemMeta
        signMeta?.setDisplayName("§6Are you sure you want to buy this claim?")
        val signMetaLore = mutableListOf<String>()
        signMetaLore.add("§2Seller: §f${seller}")
        signMetaLore.add("§2Price: §f${price}")
        signMeta?.lore = signMetaLore
        signItem.setItemMeta(signMeta)

        val redWoolItem = ItemStack(Material.RED_WOOL)
        val redWoolMeta = redWoolItem.itemMeta
        redWoolMeta?.setDisplayName("§cCancel")
        redWoolItem.setItemMeta(redWoolMeta)


        val greenWoolItem = ItemStack(Material.GREEN_WOOL)
        val greenWoolMeta = greenWoolItem.itemMeta
        greenWoolMeta?.setDisplayName("§aBuy Claim for ${price}")
        greenWoolItem.setItemMeta(greenWoolMeta)

        // Set items in specific slots
        inventory.setItem(4, signItem) // Middle slot
        inventory.setItem(3, redWoolItem) // Left of the sign
        inventory.setItem(5, greenWoolItem) // Right of the sign

        return inventory
    }

}