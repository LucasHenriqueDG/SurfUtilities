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

        val prices = plugin.currentShop[player]?.get("exactPrice") as List<*>

        val menu = Bukkit.createInventory(null, 9, "${seller.name}'s shop")

        if(type == "both"){

            val sellOne = ItemStack(Material.RED_WOOL)
            val sellOneMeta = sellOne.itemMeta
            sellOneMeta?.setDisplayName(plugin.config.getString("sell-item-name-text")?.replace(
                "%sellprice%", prices[1].toString())
            )

            val loreOne = mutableListOf<String>()
            for(text in (plugin.config.getList("sell-item-lore"))!!){
                loreOne.add(text.toString())
            }
            sellOneMeta?.lore = loreOne

            sellOne.setItemMeta(sellOneMeta)

            val buyOneItem = ItemStack(Material.GREEN_WOOL)
            val buyOneItemMeta = buyOneItem.itemMeta
            buyOneItemMeta?.setDisplayName(plugin.config.getString("buy-item-name-text")?.replace(
                "%buyprice%", prices[0].toString())
            )


            val loreTwo = mutableListOf<String>()
            for(text in (plugin.config.getList("buy-item-lore"))!!){
                loreTwo.add(text.toString())
            }
            buyOneItemMeta?.lore = loreTwo

            buyOneItem.setItemMeta(buyOneItemMeta)

            menu.setItem(2, sellOne)
            menu.setItem(4, item)
            menu.setItem(6, buyOneItem)

        } else if(type == "buy"){

            val buyOneItem = ItemStack(Material.GREEN_WOOL)
            val buyOneItemMeta = buyOneItem.itemMeta
            buyOneItemMeta?.setDisplayName(plugin.config.getString("buy-item-name-text")?.replace(
                "%buyprice%", prices[0].toString())
            )

            val loreTwo = mutableListOf<String>()
            for(text in (plugin.config.getList("buy-item-lore"))!!){
                loreTwo.add(text.toString())
            }
            buyOneItemMeta?.lore = loreTwo

            buyOneItem.setItemMeta(buyOneItemMeta)

            menu.setItem(4, item)
            menu.setItem(6, buyOneItem)

        } else{

            val sellOne = ItemStack(Material.RED_WOOL)
            val sellOneMeta = sellOne.itemMeta
            sellOneMeta?.setDisplayName(plugin.config.getString("sell-item-name-text")?.replace(
                "%sellprice%", prices[0].toString())
            )

            val loreOne = mutableListOf<String>()
            for(text in (plugin.config.getList("sell-item-lore"))!!){
                loreOne.add(text.toString().replace("%sellprice%", prices[0].toString()))
            }
            sellOneMeta?.lore = loreOne

            sellOne.setItemMeta(sellOneMeta)

            menu.setItem(2, sellOne)
            menu.setItem(4, item)

        }


        return menu

    }

}