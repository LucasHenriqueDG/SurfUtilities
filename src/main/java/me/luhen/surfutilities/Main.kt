package me.luhen.surfutilities

import me.luhen.surfutilities.commands.SellCommand
import me.luhen.surfutilities.commands.ShopMenuCommand
import me.luhen.surfutilities.listeners.*
import me.luhen.surfutilities.utils.JsonUtils
import me.luhen.surfutilities.utils.VaultUtils
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    var economy: Economy? = null
    var currentShop = mutableMapOf<Player, MutableMap<String,Any>>()
    var playersWithMenuOff = mutableListOf<Player>()

    companion object{
        lateinit var instance: Main
    }

    init {
        instance = this
    }

    val curentClaim = mutableMapOf<Player, Location>()

    override fun onEnable() {

        saveDefaultConfig()

        JsonUtils.createJsonFile("data/SignLocations.json")


        if (!VaultUtils.setupEconomy()) {
            logger.severe("Vault not found! Disabling plugin.")
            server.pluginManager.disablePlugin(this)
            return
        }

        // Plugin startup logic

        if (isPluginInstalled("GriefPrevention")) {
            getCommand("sellclaim")?.setExecutor(SellCommand)
            server.pluginManager.registerEvents(SignClickEvent, this)
            server.pluginManager.registerEvents(SignBreakListener, this)

        } else {
            logger.info("GriefPrevention is not installed. Some features will be disabled.")
        }

        if (isPluginInstalled("ChestShop")) {
            getCommand("shopmenu")?.setExecutor(ShopMenuCommand)
            server.pluginManager.registerEvents(ShopInteraction, this)
            server.pluginManager.registerEvents(ShopCreationEvent, this)

        } else {
            logger.info("GriefPrevention is not installed. Some features will be disabled.")
        }

        server.pluginManager.registerEvents(OnInventoryClick, this)

    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    private fun isPluginInstalled(pluginName: String): Boolean {
        val plugin: Plugin? = Bukkit.getPluginManager().getPlugin(pluginName)
        return plugin != null && plugin.isEnabled
    }
}
