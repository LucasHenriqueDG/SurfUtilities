package me.luhen.surfutilities.utils

import com.Acrobot.ChestShop.Database.Account
import me.luhen.surfutilities.Main
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object ShopMenu {

    val plugin = Main.instance

    fun showShopMenuView(player: Player, item: ItemStack, seller: Account, type: String): Inventory{

        val menu = Bukkit.createInventory(null, 9, "${seller.name}'s shop")

        val sellOne = ItemStack(Material.RED_WOOL)
        val sellOneMeta = sellOne.itemMeta
        sellOneMeta?.setDisplayName("§cSell 1")
        sellOne.setItemMeta(sellOneMeta)

        val buyOneItem = ItemStack(Material.GREEN_WOOL)
        val buyOneItemMeta = buyOneItem.itemMeta
        buyOneItemMeta?.setDisplayName("§aBuy 1 from ${seller.name}")
        buyOneItem.setItemMeta(buyOneItemMeta)

        if(type == "both"){

            menu.setItem(2, sellOne)
            menu.setItem(4, item)
            menu.setItem(6, buyOneItem)

        } else if(type == "buy"){

            menu.setItem(4, item)
            menu.setItem(6, buyOneItem)

        } else{

            menu.setItem(2, sellOne)
            menu.setItem(4, item)

        }


        return menu

    }

}