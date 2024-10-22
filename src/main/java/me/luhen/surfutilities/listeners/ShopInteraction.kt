package me.luhen.surfutilities.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import com.Acrobot.ChestShop.Events.PreTransactionEvent
import me.luhen.surfutilities.SurfUtilities
import me.luhen.surfutilities.utils.ShopMenu.showShopMenuView
import me.luhen.surfutilities.utils.StringToPrice
import me.luhen.surfutilities.utils.Transaction
import org.bukkit.block.Sign
import org.bukkit.block.sign.Side

object ShopInteraction: Listener {

    val plugin = SurfUtilities.instance

    @EventHandler
    fun onShopInteractEvent(event: PreTransactionEvent) {

        if (!plugin.playersWithMenuOff.contains(event.client)) {

            val item = event.stock[0]
            val seller = event.ownerAccount

            event.isCancelled = true

            val shopSign = event.sign.block.state as Sign
            val priceLine = shopSign.getSide(Side.FRONT).getLine(2)
            val prices = StringToPrice.extractNumbers(priceLine)
            val transactionType = StringToPrice.checkTransactionType(priceLine)

            val transaction = Transaction(
                event.ownerAccount,
                event.ownerInventory,
                event.client,
                event.stock,
                event.sign,
                prices)

            plugin.currentShop[event.client] = transaction

            event.client.openInventory(showShopMenuView(event.client, item, seller, transactionType))

        }

    }


}