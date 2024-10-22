package me.luhen.surfutilities.utils

import com.Acrobot.ChestShop.Database.Account
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.math.BigDecimal

data class Transaction(val owner: Account, val shopInv: Inventory, val client: Player, val stock: Array<ItemStack>,
                  val sign: Sign, val price: MutableList<BigDecimal>)