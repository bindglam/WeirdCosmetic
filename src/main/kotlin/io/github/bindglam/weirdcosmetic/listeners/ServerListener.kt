package io.github.bindglam.weirdcosmetic.listeners

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.destroystokyo.paper.event.server.ServerTickStartEvent
import io.github.bindglam.weirdcosmetic.ClosetManager
import io.github.bindglam.weirdcosmetic.WeirdCosmetic
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ServerListener : Listener {
    @EventHandler
    fun onTick(event: ServerTickStartEvent){
        for(closetPlayer in ClosetManager.closets.values){
            closetPlayer.mannequin!!.spawner.updateRotation(closetPlayer.player, closetPlayer.mannequin!!.yaw, closetPlayer.mannequin!!.pitch, closetPlayer.mannequin!!.entityID)
            closetPlayer.mannequin!!.yaw += 1f
        }

        for(player in Bukkit.getOnlinePlayers()){

        }
    }
}