package me.luhen.surfutilities.listeners

import com.Acrobot.ChestShop.ChestShop
import com.Acrobot.ChestShop.Events.PreTransactionEvent
import com.Acrobot.ChestShop.Events.TransactionEvent
import com.Acrobot.ChestShop.Events.TransactionEvent.TransactionType
import me.luhen.surfutilities.SurfUtilities
import me.luhen.surfutilities.utils.SellClaimCheck.sellClaimCheckBuyer
import me.ryanhamshire.GriefPrevention.GriefPrevention
import org.bukkit.Material
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import me.luhen.surfutilities.utils.InventoryUtils
import me.luhen.surfutilities.utils.VaultUtils
import org.bukkit.ChatColor
import org.bukkit.block.sign.Side
import java.math.BigDecimal

object OnInventoryClick: Listener {

    val plugin = SurfUtilities.instance

        @EventHandler
        fun onInventoryClick(event: InventoryClickEvent) {
            // Check if the clicked inventory is our custom inventory
            if (event.view.title == "Buy Claim") {

                event.isCancelled = true

                if(event.currentItem?.type == Material.GREEN_WOOL){

                    val buyer = event.whoClicked as Player
                    val location = plugin.curentClaim[buyer]
                    val price = (plugin.curentClaim[event.whoClicked]?.block?.state as Sign).getSide(Side.FRONT).getLine(3)

                    sellClaimCheckBuyer(buyer, GriefPrevention.instance.dataStore.getClaimAt(
                        location, true, true, null), price.toInt())

                    event.whoClicked.closeInventory()

                } else if(event.currentItem?.type == Material.RED_WOOL){

                    event.whoClicked.closeInventory()

                }

             } else if (event.view.title.contains("'s shop") && event.inventory.size <= 9) {

                val buyer = event.whoClicked as Player
                val transactionInfo = plugin.currentShop[buyer]
                val shopOwner = transactionInfo!!.owner
                val ownerInventory = transactionInfo.shopInv
                val clientInventory = transactionInfo.client.inventory
                val prices = transactionInfo.price
                val stock = transactionInfo.stock
                val buyPrice: BigDecimal?
                val sellPrice: BigDecimal?


                if (prices.size > 1) {

                    buyPrice = prices[0]
                    sellPrice = prices[1]

                } else {
                    buyPrice = prices[0]
                    sellPrice = prices[0]
                }

                event.isCancelled = true

                val currentTime = System.currentTimeMillis()

                val cooldownEndTime = plugin.shopCooldownPlayers[event.whoClicked.uniqueId]

                if (!(cooldownEndTime != null && currentTime < cooldownEndTime)) {

                    if (event.currentItem?.type == Material.GREEN_WOOL) {

                        val emptySpace = InventoryUtils.availableSpaceForItem(clientInventory, stock[0]) - 320
                        val shopStock = InventoryUtils.countItem(ownerInventory, stock[0])

                        //Checks the player's inventory
                        if (emptySpace == 0) {

                            buyer.sendMessage(
                                ChatColor.translateAlternateColorCodes(
                                    '&',
                                    plugin.config.getString("inventory-full-msg")!!
                                )
                            )

                            buyer.closeInventory()

                            //Checks the player's balance
                        } else if (!VaultUtils.hasEnoughMoney(buyer, buyPrice.toDouble())) {

                            buyer.sendMessage(
                                ChatColor.translateAlternateColorCodes(
                                    '&',
                                    plugin.config.getString("not-enough-money-item-msg")!!
                                )
                            )

                            buyer.closeInventory()

                            //Checks the shop's stock
                        } else if (shopOwner.name != "Admin Shop" && shopStock == 0){

                                buyer.sendMessage(
                                    ChatColor.translateAlternateColorCodes(
                                        '&',
                                        plugin.config.getString("empty-stock-msg")!!
                                    )
                                )

                                buyer.closeInventory()


                        } else {

                            //How many items are going to be bought
                            var amount = 1

                            val newStock = stock.copyOf()

                            if(shopOwner.name == "Admin Shop"){

                                ownerInventory.contents[0].amount = 64

                            }

                            var newBuyPrice = buyPrice

                            if (event.click.isShiftClick) {

                                amount = newStock[0].maxStackSize

                                //How much space the player has to buy?
                                if (emptySpace < amount) {

                                    amount = emptySpace

                                }

                                //How much the player can afford to buy?
                                val affordableAmount = VaultUtils.calculateAffordableAmount(
                                    buyer.uniqueId,
                                    buyPrice.toDouble(), amount
                                )

                                if (affordableAmount < amount) {

                                    amount = affordableAmount

                                }

                                if(shopStock < amount && shopOwner.name != "Admin Shop"){

                                    amount = shopStock

                                }

                                newBuyPrice = amount.toBigDecimal() * newBuyPrice

                            }

                            newStock[0].amount = amount


                            //Finally, executes the transaction
                            ChestShop.callEvent(
                                TransactionEvent(
                                    PreTransactionEvent(
                                        transactionInfo.shopInv,
                                        transactionInfo.client.inventory,
                                        newStock,
                                        newBuyPrice,
                                        transactionInfo.client,
                                        transactionInfo.owner,
                                        transactionInfo.sign,
                                        TransactionType.BUY
                                    ), transactionInfo.sign
                                )
                            )

                        }

                    } else if (event.currentItem?.type == Material.RED_WOOL) {

                        val playerItemQuantity = InventoryUtils.countItem(clientInventory, stock[0])

                        val shopSpace: Int = if(shopOwner.name != "Admin Shop"){

                            InventoryUtils.availableSpaceForItem(ownerInventory, stock[0])

                        } else {

                            5000

                        }

                        //Checks the shop space
                        if (shopSpace == 0) {

                            buyer.sendMessage(
                                ChatColor.translateAlternateColorCodes(
                                    '&',
                                    plugin.config.getString("full-stock-msg")!!
                                )
                            )

                            buyer.closeInventory()

                            //Checks if the player has the items to sell
                        } else if (playerItemQuantity == 0) {

                            buyer.sendMessage(
                                ChatColor.translateAlternateColorCodes(
                                    '&',
                                    plugin.config.getString("no-items-inventory-msg")!!
                                )
                            )

                            buyer.closeInventory()

                            //Checks if the shop owner has enough money to buy the item
                        } else if(shopOwner.name != "Admin Shop" &&
                            !VaultUtils.hasEnoughMoney(shopOwner.uuid, sellPrice.toDouble())){

                                buyer.sendMessage(
                                    ChatColor.translateAlternateColorCodes(
                                        '&',
                                        plugin.config.getString("owner-no-money-msg")!!
                                    )
                                )

                                buyer.closeInventory()

                        } else {

                            var amount = 1
                            var newSellPrice = sellPrice
                            val newStock = stock.copyOf()

                            if (event.click.isShiftClick) {

                                amount = playerItemQuantity

                                //Checks if the player's item quantity is bigger than the shop space
                                if (amount > shopSpace) {

                                    amount = shopSpace

                                }

                                //Checks how many items the shop owner can afford to buy
                                val affordableQuantity: Int = if(shopOwner.name == "Admin Shop"){

                                    playerItemQuantity

                                } else {
                                    VaultUtils.calculateAffordableAmount(
                                        shopOwner.uuid,
                                        newSellPrice.toDouble(),
                                        playerItemQuantity
                                    )

                                }

                                if (amount > affordableQuantity) {

                                    amount = affordableQuantity

                                }

                                newSellPrice *= amount.toBigDecimal()

                            }

                            newStock[0].amount = amount


                            //Finally, executes the transaction
                            ChestShop.callEvent(
                                TransactionEvent(
                                    PreTransactionEvent(
                                        transactionInfo.shopInv,
                                        transactionInfo.client.inventory,
                                        newStock,
                                        newSellPrice,
                                        transactionInfo.client,
                                        transactionInfo.owner,
                                        transactionInfo.sign,
                                        TransactionType.SELL
                                    ), transactionInfo.sign
                                )
                            )


                        }

                    }

                    plugin.shopCooldownPlayers[event.whoClicked.uniqueId] = currentTime + plugin.shopCooldownTime

                }

            }

        }

}