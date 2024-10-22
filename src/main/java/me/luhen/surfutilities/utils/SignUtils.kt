package me.luhen.surfutilities.utils


import me.luhen.surfutilities.SurfUtilities
import me.ryanhamshire.GriefPrevention.GriefPrevention
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.block.Sign
import org.bukkit.block.sign.Side
import org.bukkit.entity.Player

object SignUtils {

    val plugin = SurfUtilities.instance

    fun placeSign(seller: Player, sellPrice: Int) {

        val claim = GriefPrevention.instance.dataStore.getClaimAt(seller.location, true, null)
        if(claim.ownerName == seller.name) {
            // Set the block to a sign
            val block = seller.location.block
            block.type = Material.OAK_SIGN // or Material.OAK_WALL_SIGN for wall signs

            // Get the sign state and set the text
            val state = (block.state as Sign)

            state.getSide(Side.FRONT).setLine(0, "Buy From")
            state.getSide(Side.FRONT).setLine(1, seller.name)
            state.getSide(Side.FRONT).setLine(2, "Price:")
            state.getSide(Side.FRONT).setLine(3, sellPrice.toString())


            // Update the block state to apply changes
            state.update()
        } else {
            seller.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("not-your-claim-msg")!!))
        }



    }
}