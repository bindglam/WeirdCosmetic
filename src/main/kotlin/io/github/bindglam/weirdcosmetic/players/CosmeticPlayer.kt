package io.github.bindglam.weirdcosmetic.players

import com.comphenix.protocol.wrappers.EnumWrappers
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot.*
import com.comphenix.protocol.wrappers.Pair
import io.github.bindglam.weirdcosmetic.NPC
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class CosmeticPlayer() {
    var player: Player? = null
    var npc: NPC? = null

    constructor(npc: NPC) : this() {
        this.npc = npc
    }

    constructor(player: Player) : this() {
        this.player = player
    }

    fun setEquipment(equipment: List<Pair<EnumWrappers.ItemSlot, ItemStack>>){
        if(npc != null){
            npc!!.setEquipment(equipment)
        } else {
            for(data in equipment){
                if(data.first == null) continue
                when(data.first!!){
                    MAINHAND -> player!!.equipment.setItemInMainHand(data.second)
                    OFFHAND -> player!!.equipment.setItemInOffHand(data.second)
                    FEET -> player!!.equipment.boots = data.second
                    LEGS -> player!!.equipment.leggings = data.second
                    CHEST -> player!!.equipment.chestplate = data.second
                    HEAD -> player!!.equipment.helmet = data.second
                    BODY -> player!!.equipment.chestplate = data.second
                }
            }
        }
    }
}