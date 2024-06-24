package me.luhen.surfutilities.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import com.Acrobot.ChestShop.Events.PreTransactionEvent
import me.luhen.surfutilities.Main
import me.luhen.surfutilities.utils.ShopMenu.showShopMenuView
import me.luhen.surfutilities.utils.StringToPrice
import org.bukkit.block.Sign

object ShopInteraction: Listener {

    val plugin = Main.instance

    @EventHandler
    fun onShopInteractEvent(event: PreTransactionEvent){

        val item = event.stock[0]
        val seller = event.ownerAccount

        event.isCancelled = true

        val transactionInfo = mutableMapOf<String,Any>()
        val shopSign = event.sign.block.state as Sign
        val priceLine = shopSign.getLine(2)
        val prices = StringToPrice.extractNumbers(priceLine)
        val transactionType = StringToPrice.checkTransactionType(priceLine)
        println(prices)

        transactionInfo["ownerInventory"] = event.ownerInventory
        transactionInfo["clientInventory"] = event.clientInventory
        transactionInfo["stock"] = event.stock
        transactionInfo["client"] = event.client
        transactionInfo["ownerAccount"] = event.ownerAccount
        transactionInfo["sign"] = event.sign
        transactionInfo["exactPrice"] = prices as List<*>

        plugin.currentShop[event.client] = transactionInfo

        event.client.openInventory(showShopMenuView(event.client, item, seller, transactionType))

    }


}