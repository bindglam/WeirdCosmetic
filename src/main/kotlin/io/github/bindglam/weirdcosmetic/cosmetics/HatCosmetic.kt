package io.github.bindglam.weirdcosmetic.cosmetics

import com.comphenix.protocol.wrappers.EnumWrappers
import com.comphenix.protocol.wrappers.Pair
import com.github.retrooper.packetevents.protocol.player.Equipment
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot
import io.github.bindglam.weirdcosmetic.CosmeticManager
import io.github.bindglam.weirdcosmetic.players.CosmeticPlayer
import io.github.retrooper.packetevents.util.SpigotConversionUtil
import org.bukkit.inventory.ItemStack

class HatCosmetic(cosmeticID: String, itemID: String) : AbstractCosmetic(cosmeticID, itemID) {
    override fun onEquip(cosmeticPlayer: CosmeticPlayer) {
        for(data in cosmeticPlayer.getEquipment()){
            if(data.slot == EquipmentSlot.HELMET && data.item != null){
                cosmeticPlayer.player.inventory.addItem(SpigotConversionUtil.toBukkitItemStack(data.item))
            }
        }
        cosmeticPlayer.setEquipment(listOf(Equipment(EquipmentSlot.HELMET, SpigotConversionUtil.fromBukkitItemStack(itemStack))))
    }

    override fun onTick(cosmeticPlayer: CosmeticPlayer) {
    }

    override fun onUnequip(cosmeticPlayer: CosmeticPlayer){
        cosmeticPlayer.setEquipment(listOf(Equipment(EquipmentSlot.HELMET, null)))
    }
}