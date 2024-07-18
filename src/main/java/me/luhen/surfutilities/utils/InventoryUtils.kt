package me.luhen.surfutilities.utils

import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object InventoryUtils {

    fun countItem(inventory: Inventory, itemToCheck: ItemStack): Int {
        var totalAmount = 0

        // Iterate through each slot in the inventory
        for (item in inventory.contents) {
            // Check if the slot is not empty and if it matches the item to check
            if (item != null && item.isSimilar(itemToCheck)) {
                // Sum up the amount of the item
                totalAmount += item.amount
            }
        }
        return totalAmount
    }

    fun availableSpaceForItem(inventory: Inventory, item: ItemStack): Int {
        var totalAvailableSpace = 0
        val maxStackSize = item.maxStackSize

        for (invItem in inventory.contents) {
            if (invItem == null || invItem.type == Material.AIR) {
                // Empty slot: full stack size available
                totalAvailableSpace += maxStackSize
            } else if (invItem.type == item.type) {
                // Partial stack: add remaining space in the stack
                totalAvailableSpace += maxStackSize - invItem.amount
            }
        }

        return totalAvailableSpace

    }

}