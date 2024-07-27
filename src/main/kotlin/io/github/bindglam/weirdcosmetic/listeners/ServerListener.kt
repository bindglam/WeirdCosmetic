package io.github.bindglam.weirdcosmetic.listeners

import com.destroystokyo.paper.event.server.ServerTickStartEvent
import io.github.bindglam.weirdcosmetic.CosmeticManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ServerListener : Listener {
    @EventHandler
    fun onTick(event: ServerTickStartEvent){
        for(cosmeticPlayer in CosmeticManager.cosmeticPlayers.values){
            if(cosmeticPlayer.isCloset && cosmeticPlayer.mannequin != null){
                cosmeticPlayer.mannequin!!.spawner.updateRotation(cosmeticPlayer.player, cosmeticPlayer.mannequin!!.yaw, cosmeticPlayer.mannequin!!.pitch, cosmeticPlayer.mannequin!!.entityID)
                cosmeticPlayer.mannequin!!.yaw += 1f
            }

            for(cosmetic in cosmeticPlayer.cosmetics){
                cosmetic.onTick(cosmeticPlayer)
            }

            val MHItem = cosmeticPlayer.player.inventory.itemInMainHand
            val MHCosmeticID = CosmeticManager.getCosmeticID(MHItem)
            if (MHCosmeticID != null) {
                MHItem.amount = 0
                cosmeticPlayer.cosmetics.remove(CosmeticManager.cosmetics[MHCosmeticID])
            }

            val OHItem = cosmeticPlayer.player.inventory.itemInOffHand
            val OHCosmeticID = CosmeticManager.getCosmeticID(OHItem)
            if (OHCosmeticID != null) {
                OHItem.amount = 0
                cosmeticPlayer.cosmetics.remove(CosmeticManager.cosmetics[OHCosmeticID])
            }
        }
    }
}