package me.luhen.surfutilities.commands

import me.luhen.surfutilities.SurfUtilities
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object SuCommand: CommandExecutor {

    val plugin = SurfUtilities.instance

    override fun onCommand(sender: CommandSender, command: Command, string: String, args: Array<out String>): Boolean {

        if(args[0].equals("reload", true)) {

            if (sender.hasPermission("surfutilities.adm")) {

                plugin.reloadConfig()

                sender.sendMessage("[SurfUtilities] Messages reloaded!")

            } else {

                sender.sendMessage("You don't have permission to do that.")

            }

        }

        return true

    }
}