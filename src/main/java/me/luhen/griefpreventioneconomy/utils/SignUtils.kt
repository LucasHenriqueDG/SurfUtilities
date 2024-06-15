package me.luhen.griefpreventioneconomy.utils


import me.ryanhamshire.GriefPrevention.ClaimsMode
import me.ryanhamshire.GriefPrevention.GriefPrevention
import me.ryanhamshire.GriefPrevention.PlayerData
import me.ryanhamshire.GriefPrevention.events.ClaimPermissionCheckEvent
import org.bukkit.Material
import org.bukkit.block.Sign
import org.bukkit.entity.Player

object SignUtils {

    fun placeSign(seller: Player, sellPrice: Int) {

        val claim = GriefPrevention.instance.dataStore.getClaimAt(seller.location, true, null)
        if(claim.ownerName == seller.name) {
            // Set the block to a sign
            val block = seller.location.block
            block.type = Material.OAK_SIGN // or Material.OAK_WALL_SIGN for wall signs

            // Get the sign state and set the text
            val state = block.state as Sign

            state.setLine(0, "Buy From")
            state.setLine(1, seller.name)
            state.setLine(2, "Price:")
            state.setLine(3, sellPrice.toString())


            // Update the block state to apply changes
            state.update()
        } else {
            seller.sendMessage("§cThis claim is not yours!")
        }



    }
}