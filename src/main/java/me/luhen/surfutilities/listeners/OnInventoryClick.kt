package me.luhen.surfutilities.listeners

import com.Acrobot.ChestShop.ChestShop
import com.Acrobot.ChestShop.Database.Account
import com.Acrobot.ChestShop.Events.PreTransactionEvent
import com.Acrobot.ChestShop.Events.TransactionEvent
import com.Acrobot.ChestShop.Events.TransactionEvent.TransactionType
import me.luhen.surfutilities.Main
import me.luhen.surfutilities.utils.SellClaimCheck.SellClaimCheckBuyer
import me.ryanhamshire.GriefPrevention.GriefPrevention
import org.bukkit.Material
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import me.luhen.surfutilities.utils.InventoryUtils
import me.luhen.surfutilities.utils.VaultUtils
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.math.BigDecimal

object OnInventoryClick: Listener {

    val plugin = Main.instance

        @EventHandler
        fun onInventoryClick(event: InventoryClickEvent) {
            // Check if the clicked inventory is our custom inventory
            if (event.view.title == "Buy Claim") {

                event.isCancelled = true

                if(event.currentItem?.type == Material.GREEN_WOOL){

                    val buyer = event.whoClicked as Player
                    val location = plugin.curentClaim.get(buyer)
                    val price = (plugin.curentClaim[event.whoClicked]?.block?.state as Sign).getLine(3)

                    SellClaimCheckBuyer(buyer, GriefPrevention.instance.dataStore.getClaimAt(
                        location, true, true, null), price.toInt())

                    event.whoClicked.closeInventory()

                }

             } else if (event.view.title.contains("'s shop") && event.inventory.size <= 9){

                val buyer = event.whoClicked as Player
                val transactionInfo = plugin.currentShop[buyer]
                val shopOwner = transactionInfo?.get("ownerAccount") as Account
                val ownerInventory = transactionInfo.get("ownerInventory") as Inventory
                val clientInventory = transactionInfo["clientInventory"] as Inventory
                val prices = transactionInfo["exactPrice"] as List<*>
                val stock = transactionInfo["stock"] as Array<out ItemStack>
                val buyPrice: BigDecimal?
                val sellPrice: BigDecimal?


                if(prices.size > 1){

                    buyPrice = prices[0] as BigDecimal
                    sellPrice = prices[1] as BigDecimal

                } else {
                    buyPrice = prices[0] as BigDecimal
                    sellPrice = prices[0] as BigDecimal
                }

                event.isCancelled = true

                if(event.currentItem?.type == Material.GREEN_WOOL){

                    val emptySpace = InventoryUtils.availableSpaceForItem(clientInventory, stock[0]) - 320
                    val shopStock = InventoryUtils.countItem(ownerInventory, stock[0])

                    //Checks the player's inventory
                    if (emptySpace == 0) {

                        buyer.sendMessage(plugin.config.getString("inventory-full-msg"))

                        buyer.closeInventory()

                        //Checks the player's balance
                    } else if(!VaultUtils.hasEnoughMoney(buyer,buyPrice.toDouble())){

                        buyer.sendMessage(plugin.config.getString("not-enough-money-item-msg"))

                        buyer.closeInventory()

                        //Checks the shop's stock
                    } else if(shopStock == 0){

                        buyer.sendMessage(plugin.config.getString("empty-stock-msg"))

                        buyer.closeInventory()

                    } else {

                        //How many items are going to be bought
                        var amount = 1
                        val newStock = stock.copyOf()
                        var newBuyPrice = buyPrice


                        if(event.click.isShiftClick){

                            amount = newStock[0].maxStackSize

                            //How much space the player has to buy?
                            if(emptySpace < amount){

                                amount = emptySpace

                            }

                            //How much the player can afford to buy?
                            val affordableAmount = VaultUtils.calculateAffordableAmount(
                                buyer.uniqueId,
                                buyPrice.toDouble()
                                ,amount)

                            if(affordableAmount < amount){

                                amount = affordableAmount

                            }

                            newBuyPrice = amount.toBigDecimal() * newBuyPrice

                        }

                        newStock[0].amount = amount

                        //Finally, executes the transaction
                        ChestShop.callEvent(
                            TransactionEvent(
                                PreTransactionEvent(
                                    transactionInfo["ownerInventory"] as Inventory,
                                    transactionInfo["clientInventory"] as Inventory,
                                    newStock,
                                    newBuyPrice,
                                    transactionInfo["client"] as Player,
                                    transactionInfo["ownerAccount"] as Account,
                                    transactionInfo["sign"] as Sign,
                                    TransactionType.BUY
                                ), transactionInfo["sign"] as Sign
                            )
                        )

                    }

                } else if(event.currentItem?.type == Material.RED_WOOL){

                    val playerItemQuantity = InventoryUtils.countItem(clientInventory, stock[0])
                    val shopSpace = InventoryUtils.availableSpaceForItem(ownerInventory, stock[0])

                    //Checks the shop space
                    if(shopSpace == 0){

                        buyer.sendMessage(plugin.config.getString("full-stock-msg"))

                        buyer.closeInventory()

                        //Checks if the player has the items to sell
                    } else if(playerItemQuantity == 0){

                        buyer.sendMessage(plugin.config.getString("no-items-inventory-msg"))

                        buyer.closeInventory()

                        //Checks if the shop owner has enough money to buy the item
                    } else if(!VaultUtils.hasEnoughMoney(shopOwner.uuid, sellPrice.toDouble())){

                        buyer.sendMessage(plugin.config.getString("owner-no-money-msg"))

                        buyer.closeInventory()

                    } else {

                        var amount = 1
                        var newSellPrice = sellPrice
                        val newStock = stock.copyOf()

                        if(event.click.isShiftClick){

                            amount = playerItemQuantity

                            //Checks if the player's item quantity is bigger than the shop space
                            if(amount > shopSpace){

                                amount = shopSpace

                            }

                            //Checks how many items the shop owner can afford to buy
                            val affordableQuantity = VaultUtils.calculateAffordableAmount(
                                shopOwner.uuid,
                                newSellPrice.toDouble(),
                                playerItemQuantity
                            )

                            if(amount > affordableQuantity){

                                amount = affordableQuantity

                            }

                            newSellPrice *= amount.toBigDecimal()

                        }

                        newStock[0].amount = amount


                        //Finally, executes the transaction
                        ChestShop.callEvent(
                            TransactionEvent(
                                PreTransactionEvent(
                                    transactionInfo["ownerInventory"] as Inventory,
                                    transactionInfo["clientInventory"] as Inventory,
                                    newStock,
                                    newSellPrice,
                                    transactionInfo["client"] as Player,
                                    transactionInfo["ownerAccount"] as Account,
                                    transactionInfo["sign"] as Sign,
                                    TransactionType.SELL
                                ), transactionInfo["sign"] as Sign
                            )
                        )


                    }

                }

             }

        }
}