package me.luhen.surfutilities.listeners

import com.Acrobot.ChestShop.Events.PreShopCreationEvent
import me.luhen.surfutilities.Main
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack

object ShopCreationEvent: Listener {

    val plugin = Main.instance

    @EventHandler
    fun onShopCreation(event: PreShopCreationEvent){

        if(event.sign.type != Material.OAK_WALL_SIGN){

            event.isCancelled = true

            event.player.sendMessage(
                ChatColor.translateAlternateColorCodes('&', plugin.config.getString("shop-creation-sign-error-msg")!!))

            event.sign.block.type = Material.AIR
            Bukkit.getWorld(event.sign.location.world!!.name)!!.dropItemNaturally(event.sign.location, ItemStack(Material.OAK_SIGN))


        } else if(event.signLines[1] != "1"){

            event.isCancelled = true

            event.player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("shop-creation-error-msg")!!))

            event.sign.block.type = Material.AIR
            Bukkit.getWorld(event.sign.location.world!!.name)!!.dropItemNaturally(event.sign.location, ItemStack(Material.OAK_SIGN))

        }

    }

}