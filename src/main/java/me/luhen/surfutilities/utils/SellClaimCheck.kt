package me.luhen.surfutilities.utils

import me.luhen.surfutilities.Main
import me.ryanhamshire.GriefPrevention.Claim
import me.ryanhamshire.GriefPrevention.GriefPrevention
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import java.io.File
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


        if (VaultUtils.hasEnoughMoney(buyer, price.toDouble())) {

            signLocation?.block?.setType(Material.AIR)


            if ((sellerPlayer is Player)) {
                sellerPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("sold-claim-msg")?.replace("%buyer%", buyer.name)!!))
            }

            GriefPrevention.instance.dataStore.changeClaimOwner(claim, buyer.uniqueId)

            VaultUtils.transferMoney(buyer, seller, price.toDouble())

            val file = File(
                Bukkit.getServer().pluginManager.getPlugin("SurfUtilities")!!.dataFolder,
                "data/SignLocations.json")

            JsonUtils.removeClaimEntryById(file, claim.id)

            buyer.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("purchase-msg")!!))

        } else {
            buyer.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("not-enough-money-msg")!!))
        }
    }

}