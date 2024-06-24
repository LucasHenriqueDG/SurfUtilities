package me.luhen.surfutilities.utils

import me.luhen.surfutilities.Main
import me.ryanhamshire.GriefPrevention.Claim
import me.ryanhamshire.GriefPrevention.GriefPrevention
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.UUID

object SellClaimCheck {

    val plugin = Main.instance
    fun SellClaimCheckBuyer(buyer: Player, claim: Claim, price: Int) {
        val signLocation = plugin.curentClaim[buyer]
        val seller = claim.getOwnerID()


        fun getPlayerByUUID(uuid: UUID): Any {
            val player: Player? = Bukkit.getPlayer(uuid)
            return if (player != null && player.isOnline) {
                player
            } else {
                Bukkit.getOfflinePlayer(uuid)
            }
        }

        val econ: Economy? = null

        val sellerPlayer = getPlayerByUUID(seller)


        if (VaultUtils.hasEnoughMoney(buyer, seller, price.toDouble())) {

            signLocation?.block?.setType(Material.AIR)


            if ((sellerPlayer is Player)) {
                sellerPlayer.sendMessage("§2You've sold your claim for ${buyer.name}")
            }

            GriefPrevention.instance.dataStore.changeClaimOwner(claim, buyer.uniqueId)
            VaultUtils.transferMoney(buyer, seller, price.toDouble())
            buyer.sendMessage("§aYou've purchased this claim!")

        } else {
            buyer.sendMessage("§cYou don't have enough money to buy this claim")
        }
    }

}