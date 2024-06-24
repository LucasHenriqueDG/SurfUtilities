package me.luhen.surfutilities

import me.luhen.surfutilities.commands.SellCommand
import me.luhen.surfutilities.listeners.OnInventoryClick
import me.luhen.surfutilities.listeners.ShopInteraction
import me.luhen.surfutilities.listeners.SignClickEvent
import me.luhen.surfutilities.utils.VaultUtils
import net.milkbowl.vault.economy.Economy
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    var economy: Economy? = null
    var currentShop = mutableMapOf<Player, MutableMap<String,Any>>()

    companion object{
        lateinit var instance: Main
    }

    init {
        instance = this
    }

    val curentClaim = mutableMapOf<Player, Location>()

    override fun onEnable() {

        if (!VaultUtils.setupEconomy()) {
            logger.severe("Vault not found! Disabling plugin.")
            server.pluginManager.disablePlugin(this)
            return
        }

        // Plugin startup logic
        getCommand("venderclaim")?.setExecutor(SellCommand)
        server.pluginManager.registerEvents(SignClickEvent, this)
        server.pluginManager.registerEvents(OnInventoryClick, this)
        server.pluginManager.registerEvents(ShopInteraction, this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
