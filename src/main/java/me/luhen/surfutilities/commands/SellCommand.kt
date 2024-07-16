package me.luhen.surfutilities.commands

import me.luhen.surfutilities.Main
import me.luhen.surfutilities.utils.JsonUtils
import me.luhen.surfutilities.utils.SignUtils
import me.ryanhamshire.GriefPrevention.GriefPrevention
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.File

object SellCommand: CommandExecutor {

    val plugin = Main.instance

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        // Check if the sender is a player
        if (sender !is Player) {
            sender.sendMessage("Only players can use this command.")
            return true
        }

        val player = sender as Player

        // Check if the arguments are provided
        if (args.isEmpty()) {
            player.sendMessage("Please provide a price as the first argument.")
            return true
        }

        // Attempt to parse the first argument as an integer
        val sellPrice = args[0].toIntOrNull()

        if (sellPrice == null) {
            player.sendMessage(plugin.config.getString("invalid-price-msg") ?: "Invalid price. Please enter a valid number.")
            return true
        }

        // Get the block below the player's feet
        val blockBelow: Block = player.location.subtract(0.0, 1.0, 0.0).block

        // Check if the player has an oak sign in their inventory and is standing on glowstone
        if (player.inventory.contains(Material.OAK_SIGN) && blockBelow.type == Material.GLOWSTONE) {
            // Check if the player's current block location is air
            if (player.location.block.type == Material.AIR) {

                if(GriefPrevention.instance.dataStore.getClaimAt(
                        player.location, true, true, null) == null) {
                    player.sendMessage("There's no claim here.")

                } else if(GriefPrevention.instance.dataStore.getClaimAt(
                        player.location, true, true, null).ownerName != player.name){

                    player.sendMessage("This claim is not  yours.")

                } else {
                    SignUtils.placeSign(player, sellPrice)

                    player.sendMessage("Sign placed successfully!")

                    val file = File(Bukkit.getServer().pluginManager.getPlugin("SurfUtilities")!!
                        .dataFolder, "data/SignLocations.json")

                    val claimId = GriefPrevention.instance.dataStore.getClaimAt(
                        player.location, true, true, null).id

                    JsonUtils.saveClaimToJson(file, claimId, player.location, args[0].toInt())

                }

            } else {
                player.sendMessage("The block where you want to place the sign is not empty.")
            }
        } else {
            player.sendMessage(plugin.config.getString("shop-creation-fail-msg") ?: "Failed to create shop. Ensure you are standing on glowstone and have an oak sign in your inventory.")
        }

        return true
    }

}