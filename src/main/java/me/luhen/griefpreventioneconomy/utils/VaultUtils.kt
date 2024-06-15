package me.luhen.griefpreventioneconomy.utils

import me.luhen.griefpreventioneconomy.Main
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.plugin.RegisteredServiceProvider
import java.util.*

object VaultUtils {

    val plugin = Main.instance

    fun setupEconomy(): Boolean {
        if (plugin.server.pluginManager.getPlugin("Vault") == null) {
            return false
        }
        val rsp: RegisteredServiceProvider<Economy> = plugin.server.servicesManager.getRegistration(Economy::class.java)
            ?: return false
        plugin.economy = rsp.provider
        return plugin.economy != null
    }

    fun hasEnoughMoney(sender: Player, receiverUUID: UUID, amount: Double): Boolean {
        val economy: Economy? = plugin.economy

        if (economy == null) {
            sender.sendMessage("Economy service is not available.")
            return false
        }

        // Check if the receiver is online
        val receiverPlayer: Player? = Bukkit.getPlayer(receiverUUID)
        if (receiverPlayer != null && receiverPlayer.isOnline) {
            // Receiver is online, check their balance directly
            val receiverBalance = economy.getBalance(receiverPlayer)
            return receiverBalance >= amount
        } else {
            // Receiver is offline, use OfflinePlayer to check balance
            val offlineReceiver: OfflinePlayer = Bukkit.getOfflinePlayer(receiverUUID)
            val receiverBalance = economy.getBalance(offlineReceiver)
            return receiverBalance >= amount
        }
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
            if (withdrawResult.transactionSuccess()) {
                val depositResult = economy.depositPlayer(receiverPlayer, amount)
                if (depositResult.transactionSuccess()) {
                    println("Successfully transferred $amount to ${receiverPlayer.name}.")
                    receiverPlayer.sendMessage("You received $amount from ${sender.name}.")
                    return true
                } else {
                    economy.depositPlayer(sender, amount) // Refund sender if deposit fails
                    println("Failed to transfer money to ${receiverPlayer.name}.")
                    return false
                }
            } else {
                println("Failed to withdraw money from your account.")
                return false
            }
        } else {
            // Receiver is offline, use OfflinePlayer to transfer money
            val offlineReceiver: OfflinePlayer = Bukkit.getOfflinePlayer(receiverUUID)
            val withdrawResult = economy.withdrawPlayer(sender, amount)
            if (withdrawResult.transactionSuccess()) {
                val depositResult = economy.depositPlayer(offlineReceiver, amount)
                if (depositResult.transactionSuccess()) {
                    println("Successfully transferred $amount to ${offlineReceiver.name}.")
                    return true
                } else {
                    economy.depositPlayer(sender, amount) // Refund sender if deposit fails
                    println("Failed to transfer money to ${offlineReceiver.name}.")
                    return false
                }
            } else {
                println("Failed to withdraw money from your account.")
                return false
            }
        }
    }

}