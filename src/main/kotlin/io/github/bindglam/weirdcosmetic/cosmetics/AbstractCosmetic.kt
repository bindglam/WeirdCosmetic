package io.github.bindglam.weirdcosmetic.cosmetics

import dev.lone.itemsadder.api.CustomStack
import io.github.bindglam.weirdcosmetic.WeirdCosmetic
import io.github.bindglam.weirdcosmetic.players.CosmeticPlayer
import io.github.bindglam.weirdcosmetic.utils.DependType
import io.th0rgal.oraxen.api.OraxenItems
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class AbstractCosmetic(val itemID: String) {
    val item: ItemStack = if(WeirdCosmetic.DEPEND_TYPE == DependType.ORAXEN){
        OraxenItems.getItemById(itemID).build()
    } else {
        CustomStack.getInstance(itemID)!!.itemStack
    }

    abstract fun onTick(player: CosmeticPlayer)
}