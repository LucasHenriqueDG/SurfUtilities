package me.luhen.surfutilities.utils

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import org.bukkit.Bukkit
import org.bukkit.Location
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException


object JsonUtils {

    //Creates a JSON file at a specific path
    fun createJsonFile(filePath: String) {

        try {
            val file = File(Bukkit.getServer().pluginManager.getPlugin("SurfUtilities")!!.dataFolder, filePath)
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
                println("[Surf] File for saving sign locations successfully created.")
            }
        } catch (e: IOException) {
            println("[Surf] Error creating JSON file: ${e.message}")
        }
    }

    fun saveSignLocationToJson(file: File, playerName: String, location: Location) {
        // Define the data structure
        val newSignLocation = SignLocation(
            playerName = playerName,
            worldName = location.world?.name ?: "unknown",
            x = location.x,
            y = location.y,
            z = location.z
        )

        // Initialize Gson
        val gson = Gson()

        // Read existing data from JSON file
        val existingData: MutableList<SignLocation> = if (file.exists()) {
            val reader = FileReader(file)
            gson.fromJson(reader, object : TypeToken<MutableList<SignLocation>>() {}.type) ?: mutableListOf()
        } else {
            mutableListOf()
        }

        // Add new data to the existing list
        existingData.add(newSignLocation)

        // Write updated data back to the JSON file
        val writer = FileWriter(file)
        gson.toJson(existingData, writer)
        writer.flush()
        writer.close()
    }

    fun hasPlayerSignAtLocation(file: File, playerName: String, location: Location): Boolean {
        // Initialize Gson
        val gson = Gson()

        // Read existing data from the JSON file
        val existingData: List<SignLocation> = if (file.exists()) {
            val reader = FileReader(file)
            gson.fromJson(reader, object : TypeToken<List<SignLocation>>() {}.type) ?: emptyList()
        } else {
            emptyList()
        }

        // Search for matching sign
        return existingData.any {
            it.playerName == playerName && it.matchesLocation(location)
        }
    }


}