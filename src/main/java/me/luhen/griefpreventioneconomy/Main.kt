package me.luhen.griefpreventioneconomy

import me.luhen.griefpreventioneconomy.commands.SellCommand
import me.luhen.griefpreventioneconomy.listeners.OnInventoryClick
import me.luhen.griefpreventioneconomy.listeners.SignClickEvent
import me.luhen.griefpreventioneconomy.utils.VaultUtils
import net.milkbowl.vault.economy.Economy
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    var economy: Economy? = null

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
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
