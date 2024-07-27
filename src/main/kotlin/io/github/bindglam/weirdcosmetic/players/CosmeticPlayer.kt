package io.github.bindglam.weirdcosmetic.players

import com.comphenix.protocol.wrappers.Pair
import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.protocol.player.Equipment
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot.*
import io.github.bindglam.weirdcosmetic.NPC
import io.github.bindglam.weirdcosmetic.cosmetics.AbstractCosmetic
import io.github.retrooper.packetevents.util.SpigotConversionUtil
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

    fun setEquipment(equipment: List<Equipment>){
        if(mannequin != null)
            mannequin!!.equipment = equipment
        for(data in equipment){
            if(data.slot == null || data.item == null) continue
            when(data.slot!!){
                MAIN_HAND -> player.equipment.setItemInMainHand(SpigotConversionUtil.toBukkitItemStack(data.item))
                OFF_HAND -> player.equipment.setItemInOffHand(SpigotConversionUtil.toBukkitItemStack(data.item))
                BOOTS -> player.equipment.boots = SpigotConversionUtil.toBukkitItemStack(data.item)
                LEGGINGS -> player.equipment.leggings = SpigotConversionUtil.toBukkitItemStack(data.item)
                CHEST_PLATE -> player.equipment.chestplate = SpigotConversionUtil.toBukkitItemStack(data.item)
                HELMET -> player.equipment.helmet = SpigotConversionUtil.toBukkitItemStack(data.item)
                BODY -> player.equipment.chestplate = SpigotConversionUtil.toBukkitItemStack(data.item)
            }
        }
    }

    fun getEquipment(): List<Equipment>{
        return listOf(
            Equipment(HELMET, SpigotConversionUtil.fromBukkitItemStack(player.inventory.helmet ?: ItemStack(Material.AIR))),
            Equipment(CHEST_PLATE, SpigotConversionUtil.fromBukkitItemStack(player.inventory.chestplate ?: ItemStack(Material.AIR))),
            Equipment(BODY, SpigotConversionUtil.fromBukkitItemStack(player.inventory.chestplate ?: ItemStack(Material.AIR))),
            Equipment(LEGGINGS, SpigotConversionUtil.fromBukkitItemStack(player.inventory.leggings ?: ItemStack(Material.AIR))),
            Equipment(BOOTS, SpigotConversionUtil.fromBukkitItemStack(player.inventory.boots ?: ItemStack(Material.AIR))),
            Equipment(MAIN_HAND, SpigotConversionUtil.fromBukkitItemStack(player.inventory.itemInMainHand)),
            Equipment(OFF_HAND, SpigotConversionUtil.fromBukkitItemStack(player.inventory.itemInOffHand)),
        )
    }
}