package io.github.bindglam.weirdcosmetic.guis

import io.github.bindglam.weirdcosmetic.CosmeticManager
import io.github.bindglam.weirdcosmetic.WeirdCosmetic
import io.github.bindglam.weirdcosmetic.cosmetics.AbstractCosmetic
import io.th0rgal.oraxen.items.ItemBuilder
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.InventoryView
import org.bukkit.persistence.PersistentDataType

class ClosetGui(private val page: Int) : InventoryHolder, Listener {
    private val inv = Bukkit.createInventory(null, 9*6, Component.text("옷장 ( $page 페이지 )"))

    private var view: InventoryView? = null

    init {
        Bukkit.getPluginManager().registerEvents(this, WeirdCosmetic.INSTANCE)

        inv.setItem(9*5, ItemBuilder(Material.ARROW).setItemName("<white><bold>이전").build())
        inv.setItem(9*5+4, ItemBuilder(Material.BARRIER).setItemName("<red><bold>닫기").build())
        inv.setItem(9*5+8, ItemBuilder(Material.ARROW).setItemName("<white><bold>다음").build())

        for(i in (page*45)..<((page+1)*45)){
            if(CosmeticManager.cosmetics.size <= i) break
            val cosmetic = CosmeticManager.cosmetics.values.stream().toList()[i]!!
            inv.setItem(i-(page*45), cosmetic.itemStack.clone().apply {
                itemMeta = itemMeta.apply {
                    val dataContainer = persistentDataContainer
                    dataContainer.set(NamespacedKey(WeirdCosmetic.INSTANCE, "is-closet"), PersistentDataType.BOOLEAN, true)
                }
            })
        }
    }

    @EventHandler
    fun onClick(event: InventoryClickEvent){
        val player = event.whoClicked as Player
        val cosmeticPlayer = CosmeticManager.cosmeticPlayers[player.uniqueId]!!
        val itemStack = event.currentItem ?: return
        val itemMeta = itemStack.itemMeta ?: return
        val dataContainer = itemMeta.persistentDataContainer
        val slot = event.rawSlot
        if(event.view != view) return
        event.isCancelled = true

        if(slot == 9*5 && page >= 1){
            ClosetGui(page-1).open(player)
        } else if(slot == 9*5+8){
            ClosetGui(page+1).open(player)
        } else if(slot == 9*5+4){
            player.closeInventory()
        } else if(dataContainer.has(NamespacedKey(WeirdCosmetic.INSTANCE, "is-closet"))) {
            val cosmeticID = dataContainer.get(NamespacedKey(WeirdCosmetic.INSTANCE, "cosmetic-id"), PersistentDataType.STRING)!!
            val cosmetic = CosmeticManager.cosmetics[cosmeticID]!!

            var sameSlotOther: AbstractCosmetic? = null
            for(other in cosmeticPlayer.cosmetics){
                if(other::class.java.simpleName == cosmetic::class.java.simpleName){
                    sameSlotOther = other
                }
            }

            if(sameSlotOther != null){
                cosmeticPlayer.cosmetics.remove(sameSlotOther)
                sameSlotOther.onUnequip(cosmeticPlayer)

                if(sameSlotOther.cosmeticID != cosmeticID){
                    cosmeticPlayer.cosmetics.add(cosmetic)

                    cosmetic.onEquip(cosmeticPlayer)
                }
            } else {
                cosmeticPlayer.cosmetics.add(cosmetic)

                cosmetic.onEquip(cosmeticPlayer)
            }
        }
    }

    fun open(player: Player){
        view = player.openInventory(inventory)
    }

    override fun getInventory(): Inventory {
        return inv
    }
}