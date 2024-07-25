package io.github.bindglam.weirdcosmetic.guis

import io.github.bindglam.weirdcosmetic.ClosetManager
import io.github.bindglam.weirdcosmetic.WeirdCosmetic
import io.th0rgal.oraxen.items.ItemBuilder
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.InventoryView

class ClosetGui(private val page: Int) : InventoryHolder, Listener {
    private val inv = Bukkit.createInventory(null, 9*6, Component.text("옷장 ( $page 페이지 )"))
    private var view: InventoryView? = null

    init {
        Bukkit.getPluginManager().registerEvents(this, WeirdCosmetic.INSTANCE)

        inv.setItem(9*5, ItemBuilder(Material.ARROW).setItemName("<white><bold>뒤로").build())
        inv.setItem(9*5+8, ItemBuilder(Material.ARROW).setItemName("<white><bold>다음").build())
        inv.setItem(9*5+4, ItemBuilder(Material.BARRIER).setItemName("<red><bold>뒤로").build())

        for(i in 9*5*page..<9*5*(page+1)){
            val cosmetic = ClosetManager.cosmetics.values.stream().toList()[i]
            inv.setItem(i, cosmetic.item)
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent){
        val player = event.whoClicked as Player
        val itemStack = event.currentItem ?: return
        val view = event.view
        if(this.view != view) return
        val inventory = view.topInventory
        val slot = event.slot
        event.isCancelled = true

        if(slot == 9*5 && page > 0){
            ClosetGui(page-1).open(player)
        } else if(slot == 9*5+8){
            ClosetGui(page+1).open(player)
        } else if(slot == 9*5+4){
            TODO()
        }
    }

    fun open(player: Player){
        view = player.openInventory(inventory)
    }

    override fun getInventory(): Inventory {
        return inv
    }
}