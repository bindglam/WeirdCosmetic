package io.github.bindglam.weirdcosmetic.cosmetics

import com.comphenix.protocol.wrappers.EnumWrappers
import com.comphenix.protocol.wrappers.Pair
import io.github.bindglam.weirdcosmetic.CosmeticManager
import io.github.bindglam.weirdcosmetic.players.CosmeticPlayer
import org.bukkit.inventory.ItemStack

class HatCosmetic(cosmeticID: String, itemID: String) : AbstractCosmetic(cosmeticID, itemID) {
    override fun onEquip(cosmeticPlayer: CosmeticPlayer) {
        for(pair in cosmeticPlayer.getEquipment()){
            if(pair.first == EnumWrappers.ItemSlot.HEAD && pair.second != null){
                cosmeticPlayer.player.inventory.addItem(pair.second)
            }
        }
        cosmeticPlayer.setEquipment(listOf(Pair(EnumWrappers.ItemSlot.HEAD, itemStack)))
    }

    override fun onTick(cosmeticPlayer: CosmeticPlayer) {
    }

    override fun onUnequip(cosmeticPlayer: CosmeticPlayer){
        cosmeticPlayer.setEquipment(listOf(Pair(EnumWrappers.ItemSlot.HEAD, null)))
    }
}