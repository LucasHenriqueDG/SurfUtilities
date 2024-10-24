package me.luhen.surfutilities.commands

import me.luhen.surfutilities.SurfUtilities
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object ShopMenuCommand: CommandExecutor {

    val plugin = SurfUtilities.instance
    override fun onCommand(sender: CommandSender, command: Command, string: String, args: Array<out String>): Boolean {

        if(sender is Player){

            if(plugin.playersWithMenuOff.contains(sender)){

                plugin.playersWithMenuOff.remove(sender)

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("menu-on-msg")!!))

            } else {

                plugin.playersWithMenuOff.add(sender)

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("menu-off-msg")!!))

            }

        }

        return true

    }


}