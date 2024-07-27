package io.github.bindglam.weirdcosmetic.listeners

import io.github.bindglam.weirdcosmetic.CosmeticManager
import io.github.bindglam.weirdcosmetic.WeirdCosmetic
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class InventoryListener : Listener {
    @EventHandler
    fun onClick(event: InventoryClickEvent){
        val player = event.whoClicked as Player
        val itemStack = event.currentItem ?: return
        val itemMeta = itemStack.itemMeta ?: return
        val dataContainer = itemMeta.persistentDataContainer
        val cosmeticID = CosmeticManager.getCosmeticID(itemStack) ?: return

        if(!dataContainer.has(NamespacedKey(WeirdCosmetic.INSTANCE, "is-closet"))){
            event.isCancelled = true
        }
    }
}