package me.luhen.surfutilities.commands

import me.luhen.surfutilities.SurfUtilities
import me.luhen.surfutilities.utils.JsonUtils
import me.luhen.surfutilities.utils.SignUtils
import me.ryanhamshire.GriefPrevention.GriefPrevention
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.File

object SellCommand: CommandExecutor {

    val plugin = SurfUtilities.instance

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

        // Check if the arguments are provided
        if (args.isEmpty()) {
            sender.sendMessage("Please provide a price as the first argument.")
            return true
        }

        // Attempt to parse the first argument as an integer
        val sellPrice = args[0].toIntOrNull()

        if (sellPrice == null) {
            sender.sendMessage(
                ChatColor.translateAlternateColorCodes('&', plugin.config.getString("invalid-price-msg")!!)
            )
            return true
        }

        // Get the block below the player's feet
        val blockBelow: Block = sender.location.subtract(0.0, 1.0, 0.0).block

        // Check if the player has an oak sign in their inventory and is standing on glowstone
        if (sender.inventory.contains(Material.OAK_SIGN) && blockBelow.type == Material.GLOWSTONE) {
            // Check if the player's current block location is air
            if (sender.location.block.type == Material.AIR) {

                if(GriefPrevention.instance.dataStore.getClaimAt(
                        sender.location, true, true, null
                    ) == null) {
                    sender.sendMessage("There's no claim here.")

                } else if(GriefPrevention.instance.dataStore.getClaimAt(
                        sender.location, true, true, null
                    ).ownerName != sender.name) {

                    sender.sendMessage("This claim is not  yours.")

                } else {
                    SignUtils.placeSign(sender, sellPrice)

                    sender.sendMessage("Sign placed successfully!")

                    val file = File(
                        Bukkit.getServer().pluginManager.getPlugin("SurfUtilities")!!
                            .dataFolder, "data/SignLocations.json"
                    )

                    val claimId = GriefPrevention.instance.dataStore.getClaimAt(
                        sender.location, true, true, null
                    ).id

                    JsonUtils.saveClaimToJson(file, claimId, sender.location, args[0].toInt())

                }

            } else {
                sender.sendMessage("The block where you want to place the sign is not empty.")
            }
        } else {
            sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    plugin.config.getString("shop-creation-fail-msg")!!
                )
            )
        }

        return true
    }

}