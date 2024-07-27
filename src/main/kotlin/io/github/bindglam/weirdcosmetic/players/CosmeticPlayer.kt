package io.github.bindglam.weirdcosmetic.players

import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot.*
import com.comphenix.protocol.wrappers.Pair
import io.github.bindglam.weirdcosmetic.NPC
import io.github.bindglam.weirdcosmetic.cosmetics.AbstractCosmetic
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
        mannequin!!.spawner.addPassenger(player, mannequin!!.entityID, entity)
    }

    fun setEquipment(equipment: List<Pair<ItemSlot, ItemStack>>){
        if(mannequin != null)
            mannequin!!.equipment = equipment
        for(data in equipment){
            if(data.first == null) continue
            when(data.first!!){
                MAINHAND -> player.equipment.setItemInMainHand(data.second)
                OFFHAND -> player.equipment.setItemInOffHand(data.second)
                FEET -> player.equipment.boots = data.second
                LEGS -> player.equipment.leggings = data.second
                CHEST -> player.equipment.chestplate = data.second
                HEAD -> player.equipment.helmet = data.second
                BODY -> player.equipment.chestplate = data.second
            }
        }
    }

    fun getEquipment(): List<Pair<ItemSlot, ItemStack>>{
        return listOf(
            Pair(MAINHAND, player.equipment.itemInMainHand),
            Pair(OFFHAND, player.equipment.itemInOffHand),
            Pair(HEAD, player.equipment.helmet),
            Pair(CHEST, player.equipment.chestplate),
            Pair(LEGS, player.equipment.leggings),
            Pair(FEET, player.equipment.boots),
            Pair(BODY, player.equipment.chestplate),
        )
    }
}