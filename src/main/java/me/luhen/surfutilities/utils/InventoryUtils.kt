package me.luhen.surfutilities.utils

import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object InventoryUtils {

    fun isInventoryFull(inventory: Inventory): Boolean {
        // Iterate through each slot in the inventory
        for (item in inventory.contents) {
            // Check if the slot is empty (null or AIR)
            if (item == null || item.type == Material.AIR) {
                return false
            }
            // Check if the item stack is less than the maximum stack size
            if (item.amount < item.maxStackSize) {
                return false
            }
        }
        // If no empty or stackable slots are found, the inventory is full
        return true
    }

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