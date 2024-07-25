package io.github.bindglam.weirdcosmetic.cosmetics

import com.comphenix.protocol.wrappers.EnumWrappers
import com.comphenix.protocol.wrappers.Pair
import io.github.bindglam.weirdcosmetic.players.CosmeticPlayer
import org.bukkit.entity.Player

class HatCosmetic(itemID: String) : AbstractCosmetic("hat", itemID) {
    override fun onTick(player: CosmeticPlayer) {
        player.setEquipment(listOf(Pair(EnumWrappers.ItemSlot.HEAD, item)))
    }
}