package io.github.bindglam.weirdcosmetic

import io.github.bindglam.weirdcosmetic.cosmetics.AbstractCosmetic
import io.github.bindglam.weirdcosmetic.players.CosmeticPlayer
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.io.File
import java.util.UUID

object CosmeticManager {
    private val cosmeticPlayerConfigFile = File("plugins/WeirdCosmetic/cosmetic-player.yml")
    private var cosmeticPlayerConfig: YamlConfiguration? = null

    var cameraLoc: Location? = null
    var mannequinLoc: Location? = null

    val cosmeticPlayers = HashMap<UUID, CosmeticPlayer>()
    val cosmetics = HashMap<String, AbstractCosmetic>()
    val cosmeticClasses = HashMap<String, Class<out AbstractCosmetic>>()

    fun load(){
        if(!cosmeticPlayerConfigFile.exists())
            cosmeticPlayerConfigFile.createNewFile()
        cosmeticPlayerConfig = YamlConfiguration.loadConfiguration(cosmeticPlayerConfigFile)

        if(WeirdCosmetic.INSTANCE.config.get("locations.camera") == null) return
        cameraLoc = deserializeLocation(WeirdCosmetic.INSTANCE.config.getString("locations.camera")!!)
        mannequinLoc = deserializeLocation(WeirdCosmetic.INSTANCE.config.getString("locations.mannequin")!!)

        for(player in Bukkit.getOnlinePlayers()){
            cosmeticPlayers[player.uniqueId] = CosmeticPlayer(player)
        }
    }

    fun save(){
        if(cameraLoc == null) return
        WeirdCosmetic.INSTANCE.config.set("locations.camera", serializeLocation(cameraLoc!!))
        WeirdCosmetic.INSTANCE.config.set("locations.mannequin", serializeLocation(mannequinLoc!!))
    }

    fun loadCosmeticPlayer(uuid: UUID){
    }

    fun saveCosmeticPlayer(cosmeticPlayer: CosmeticPlayer){
        //cosmeticPlayerConfig!!.set("${cosmeticPlayer.player.uniqueId}.")
    }

    fun getCosmeticID(itemStack: ItemStack): String?{
        val meta = itemStack.itemMeta ?: return null
        val dataContainer = meta.persistentDataContainer
        if(dataContainer.has(NamespacedKey(WeirdCosmetic.INSTANCE, "cosmetic-id"))){
            return dataContainer.get(NamespacedKey(WeirdCosmetic.INSTANCE, "cosmetic-id"), PersistentDataType.STRING)
        }
        return null
    }

    fun register(cosmetic: Class<out AbstractCosmetic>){
        cosmeticClasses[cosmetic.simpleName] = cosmetic
        WeirdCosmetic.INSTANCE.logger.info("${cosmetic.simpleName} is registered!")
    }

    private fun serializeLocation(loc: Location): String{
        return "world=${loc.world.name},x=${loc.x},y=${loc.y},z=${loc.z},yaw=${loc.yaw},pitch=${loc.pitch}"
    }

    private fun deserializeLocation(data: String): Location{
        val result = Location(null, 0.0, 0.0, 0.0)
        for(section in data.split(",")){
            val key = section.split("=")[0]
            val valueStr = section.split("=")[1]
            when(key){
                "world" -> result.world = Bukkit.getWorld(valueStr)
                "x" -> result.x = valueStr.toDouble()
                "y" -> result.y = valueStr.toDouble()
                "z" -> result.z = valueStr.toDouble()
                "yaw" -> result.yaw = valueStr.toFloat()
                "pitch" -> result.pitch = valueStr.toFloat()
            }
        }
        return result
    }
}