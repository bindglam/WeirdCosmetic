package io.github.bindglam.weirdcosmetic

import io.github.bindglam.weirdcosmetic.cosmetics.AbstractCosmetic
import io.github.bindglam.weirdcosmetic.players.ClosetPlayer
import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.UUID

object ClosetManager {
    val closets = HashMap<UUID, ClosetPlayer>()
    val cosmetics = HashMap<String, AbstractCosmetic>()

    var closetCameraLoc = Location(null, 0.0, 0.0, 0.0)
    var closetMannequinLoc = Location(null, 0.0, 0.0, 0.0)

    fun load(){
        if(WeirdCosmetic.INSTANCE.config.get("closetCameraLoc") == null) return
        closetCameraLoc = deserializeLocation(WeirdCosmetic.INSTANCE.config.getString("closetCameraLoc")!!)
        closetMannequinLoc = deserializeLocation(WeirdCosmetic.INSTANCE.config.getString("closetMannequinLoc")!!)
    }

    fun save(){
        if(closetCameraLoc.world == null) return
        WeirdCosmetic.INSTANCE.config.set("closetCameraLoc", serializeLocation(closetCameraLoc))
        WeirdCosmetic.INSTANCE.config.set("closetMannequinLoc", serializeLocation(closetMannequinLoc))
    }

    private fun serializeLocation(location: Location): String{
        return "world=${location.world.name},x=${location.x},y=${location.y},z=${location.z},yaw=${location.yaw},pitch=${location.pitch}"
    }

    private fun deserializeLocation(data: String): Location{
        val location = Location(null, 0.0, 0.0, 0.0)
        for(section in data.split(",")){
            val key = section.split("=")[0]
            val value = section.split("=")[1]

            when(key){
                "world" -> location.world = Bukkit.getWorld(value)
                "x" -> location.x = value.toDouble()
                "y" -> location.y = value.toDouble()
                "z" -> location.z = value.toDouble()
                "yaw" -> location.yaw = value.toFloat()
                "pitch" -> location.pitch = value.toFloat()
            }
        }
        return location
    }
}