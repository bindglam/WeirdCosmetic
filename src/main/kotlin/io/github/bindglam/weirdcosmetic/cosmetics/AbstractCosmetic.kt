package io.github.bindglam.weirdcosmetic.cosmetics

import dev.lone.itemsadder.api.CustomStack
import io.github.bindglam.weirdcosmetic.WeirdCosmetic
import io.github.bindglam.weirdcosmetic.players.CosmeticPlayer
import io.github.bindglam.weirdcosmetic.utils.DependType.ITEMSADDER
import io.github.bindglam.weirdcosmetic.utils.DependType.ORAXEN
import io.th0rgal.oraxen.api.OraxenItems
import org.bukkit.NamespacedKey
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

abstract class AbstractCosmetic(val cosmeticID: String, val itemID: String) : Listener {
    val itemStack = when(WeirdCosmetic.DEPEND_TYPE){
        ITEMSADDER -> CustomStack.getInstance(itemID)!!.itemStack
        ORAXEN -> OraxenItems.getItemById(itemID).build()
    }.clone().apply {
        itemMeta = itemMeta.apply {
            val dataContainer = persistentDataContainer
            dataContainer.set(NamespacedKey(WeirdCosmetic.INSTANCE, "cosmetic-id"), PersistentDataType.STRING, cosmeticID)
        }
    }

    abstract fun onEquip(cosmeticPlayer: CosmeticPlayer)
    abstract fun onTick(cosmeticPlayer: CosmeticPlayer)
    abstract fun onUnequip(cosmeticPlayer: CosmeticPlayer)
}