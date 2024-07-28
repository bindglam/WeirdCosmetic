package io.github.bindglam.weirdcosmetic.players

import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot.*
import io.github.bindglam.weirdcosmetic.cosmetics.AbstractCosmetic
import io.github.bindglam.weirdcosmetic.packet.NPC
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class CosmeticPlayer(val player: Player) {
    var isCloset: Boolean = false

    var mannequin: NPC? = null

    var closetCamera: ArmorStand? = null

    val cosmetics = ArrayList<AbstractCosmetic>()

    fun addPassenger(entity: Entity){
        player.addPassenger(entity)
        //mannequin!!.spawner.addPassenger(player, mannequin!!.entityID, entity)
    }

    fun setEquipment(equipment: HashMap<ItemSlot, ItemStack>){
        if(mannequin != null){
            mannequin!!.equipment = equipment
        }
        for(slot in equipment.keys){
            when(slot){
                MAINHAND -> player.inventory.setItemInMainHand(equipment[MAINHAND])
                OFFHAND -> player.inventory.setItemInOffHand(equipment[OFFHAND])
                FEET ->  player.inventory.boots = equipment[FEET]
                LEGS -> player.inventory.leggings = equipment[LEGS]
                CHEST -> player.inventory.chestplate = equipment[CHEST]
                HEAD -> player.inventory.helmet = equipment[HEAD]
                BODY -> player.inventory.chestplate = equipment[BODY]
            }
        }
    }

    fun getEquipment(): HashMap<ItemSlot, ItemStack>{
        return hashMapOf(
            HEAD to (player.inventory.helmet ?: ItemStack.of(Material.AIR)),
            CHEST to (player.inventory.chestplate ?: ItemStack.of(Material.AIR)),
            BODY to (player.inventory.chestplate ?: ItemStack.of(Material.AIR)),
            LEGS to (player.inventory.leggings ?: ItemStack.of(Material.AIR)),
            FEET to (player.inventory.boots ?: ItemStack.of(Material.AIR)),
        )
    }
}