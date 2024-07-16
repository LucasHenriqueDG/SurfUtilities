package me.luhen.surfutilities.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import me.ryanhamshire.GriefPrevention.GriefPrevention
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

    fun saveClaimToJson(file: File, claimId: Long, location: Location, value: Int) {
        // Initialize Gson
        val gson = Gson()

        // Read existing data from JSON file
        val existingData: MutableList<ClaimEntry> = if (file.exists()) {
            val reader = FileReader(file)
            gson.fromJson(reader, object : TypeToken<MutableList<ClaimEntry>>() {}.type) ?: mutableListOf()
        } else {
            mutableListOf()
        }

        // Check if claimId already exists
        val existingClaim = existingData.find { it.claimId == claimId }
        if (existingClaim != null) {
            println("Claim ID $claimId already exists. Skipping addition.")
            return
        }

        // Convert Location to LocationData
        val locationData = LocationData.fromLocation(location)

        // Add new entry
        val newEntry = ClaimEntry(
            claimId = claimId,
            location = locationData,
            value = value
        )
        existingData.add(newEntry)

        // Write updated data back to the JSON file
        val writer = FileWriter(file)
        gson.toJson(existingData, writer)
        writer.flush()
        writer.close()
    }

    fun getClaimsFromJson(file: File): List<ClaimEntry> {
        // Initialize Gson
        val gson = Gson()

        // Read data from JSON file
        val existingData: List<ClaimEntry> = if (file.exists()) {
            val reader = FileReader(file)
            gson.fromJson(reader, object : TypeToken<List<ClaimEntry>>() {}.type) ?: emptyList()
        } else {
            emptyList()
        }

        return existingData
    }

    fun removeClaimEntryById(file: File, claimId: Long) {

        val gson = Gson()

        // Read the existing data from the JSON file
        val existingData = getClaimsFromJson(file) as MutableList

        // Find and remove the claim by claimId
        val iterator = existingData.iterator()

        var entryFound = false

        var claimEntry: ClaimEntry? = null

        while (iterator.hasNext()) {

            claimEntry = iterator.next()

            if (claimEntry.claimId == claimId) {

                entryFound = true

                break
            }
        }

        if (entryFound) {

            // Write the updated data back to the JSON file
            try {

                val writer = FileWriter(file)

                existingData.remove(claimEntry)

                gson.toJson(existingData, writer)

                writer.flush()

                writer.close()

                println("Claim with ID $claimId has been removed.")

            } catch (e: IOException) {

                println("Failed to write to file: ${e.message}")

            }

        } else {

            println("Claim with ID $claimId was not found.")

        }

    }

}