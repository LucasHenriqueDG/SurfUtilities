package me.luhen.surfutilities.utils

import me.luhen.surfutilities.SurfUtilities
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.plugin.RegisteredServiceProvider
import java.util.*
import kotlin.math.floor

object VaultUtils {

    val plugin = SurfUtilities.instance

    fun setupEconomy(): Boolean {
        if (plugin.server.pluginManager.getPlugin("Vault") == null) {
            return false
        }

        val rsp: RegisteredServiceProvider<Economy> = plugin.server.servicesManager.getRegistration(Economy::class.java)
            ?: return false
        plugin.economy = rsp.provider
        return true
    }

    fun hasEnoughMoney(sender: Player, amount: Double): Boolean {

        val economy: Economy? = plugin.economy
        val playerBalance = economy?.getBalance(sender)

        if (playerBalance != null) {

            return playerBalance >= amount

        }

        return false
    }

    fun hasEnoughMoney(receiverUUID: UUID, amount: Double): Boolean {

        val economy: Economy? = plugin.economy
        val receiver: OfflinePlayer = Bukkit.getOfflinePlayer(receiverUUID)
        val receiverBalance = economy?.getBalance(receiver)

        return if (receiverBalance != null) {

            receiverBalance >= amount

        } else {

            false

        }

    }

    fun calculateAffordableAmount(playerUUID: UUID, itemPrice: Double, desiredAmount: Int): Int {

        val economy: Economy? = plugin.economy
        val receiver: OfflinePlayer = Bukkit.getOfflinePlayer(playerUUID)
        val playerMoney = economy?.getBalance(receiver)

        // Calculate total cost for the desired amount
        val totalCost = itemPrice * desiredAmount

        // If the player can afford the desired amount, return it
        if (playerMoney != null) {
            if (totalCost <= playerMoney.toDouble()) {
                return desiredAmount
            }
        }

        // Calculate the maximum amount the player can afford
        // Using floor to ensure the amount is rounded down to the nearest integer

        return floor(playerMoney!! / itemPrice).toInt()
    }

    fun transferMoney(sender: Player, receiverUUID: UUID, amount: Double): Boolean {
        val economy: Economy? = plugin.economy

        if (economy == null) {
            sender.sendMessage("Economy service is not available.")
            return false
        }

        // Check if the receiver is online
        val receiverPlayer: Player? = Bukkit.getPlayer(receiverUUID)
        if (receiverPlayer != null && receiverPlayer.isOnline) {
            // Receiver is online, transfer money directly
            val withdrawResult = economy.withdrawPlayer(sender, amount)
            return if (withdrawResult.transactionSuccess()) {
                val depositResult = economy.depositPlayer(receiverPlayer, amount)
                if (depositResult.transactionSuccess()) {
                    receiverPlayer.sendMessage("You received $amount from ${sender.name}.")
                    true
                } else {
                    economy.depositPlayer(sender, amount) // Refund sender if deposit fails
                    false
                }
            } else {
                false
            }
        } else {
            // Receiver is offline, use OfflinePlayer to transfer money
            val offlineReceiver: OfflinePlayer = Bukkit.getOfflinePlayer(receiverUUID)
            val withdrawResult = economy.withdrawPlayer(sender, amount)
            return if (withdrawResult.transactionSuccess()) {
                val depositResult = economy.depositPlayer(offlineReceiver, amount)
                if (depositResult.transactionSuccess()) {
                    println("Successfully transferred $amount to ${offlineReceiver.name}.")
                    true
                } else {
                    economy.depositPlayer(sender, amount) // Refund sender if deposit fails
                    false
                }
            } else {
                false
            }
        }
    }

}