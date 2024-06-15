package me.luhen.griefpreventioneconomy.commands

import me.luhen.griefpreventioneconomy.utils.SignUtils
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object SellCommand: CommandExecutor {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        val seller = p0 as Player
            val sellPrice = p3[0].toIntOrNull()
            val blockBelow: Block = seller.location.subtract(0.0, 1.0, 0.0).block

            if(sellPrice != null){
                if(seller.inventory.contains(Material.OAK_SIGN) && blockBelow.type == Material.GLOWSTONE){
                    if(seller.location.block.type == Material.AIR){
                        SignUtils.placeSign(seller,sellPrice)
                    }
                } else {
                    seller.sendMessage("§cYou must have an §eoak sign §cin your inventory and stand" +
                            "above a §6glowstone §cto create a sell sign.")
                }
            } else {
                seller.sendMessage("§cInvalid price value, use something from 0 to 1000000000.")
            }

        return true
    }
}