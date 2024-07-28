package io.github.bindglam.weirdcosmetic.cosmetics

import com.comphenix.protocol.wrappers.EnumWrappers
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot
import io.github.bindglam.weirdcosmetic.players.CosmeticPlayer
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class HatCosmetic(cosmeticID: String, itemID: String) : AbstractCosmetic(cosmeticID, itemID) {
    override fun onEquip(cosmeticPlayer: CosmeticPlayer) {
        if(cosmeticPlayer.getEquipment()[ItemSlot.HEAD] != null){
            cosmeticPlayer.player.inventory.addItem(cosmeticPlayer.getEquipment()[ItemSlot.HEAD]!!)
        }
        cosmeticPlayer.setEquipment(hashMapOf(ItemSlot.HEAD to itemStack))
    }

    override fun onTick(cosmeticPlayer: CosmeticPlayer) {
    }

    override fun onUnequip(cosmeticPlayer: CosmeticPlayer){
        cosmeticPlayer.setEquipment(hashMapOf(ItemSlot.HEAD to ItemStack.of(Material.AIR)))
    }
}