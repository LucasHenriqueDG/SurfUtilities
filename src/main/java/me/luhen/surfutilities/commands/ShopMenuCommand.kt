package me.luhen.surfutilities.commands

import me.luhen.surfutilities.Main
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object ShopMenuCommand: CommandExecutor {

    val plugin = Main.instance
    override fun onCommand(sender: CommandSender, command: Command, string: String, args: Array<out String>): Boolean {

        if(sender is Player){

            if(plugin.playersWithMenuOff.contains(sender)){

                plugin.playersWithMenuOff.remove(sender)

                sender.sendMessage(plugin.config.getString("menu-on-msg"))

            } else {

                plugin.playersWithMenuOff.add(sender)

                sender.sendMessage(plugin.config.getString("menu-off-msg"))

            }

        }

        return true

    }


}