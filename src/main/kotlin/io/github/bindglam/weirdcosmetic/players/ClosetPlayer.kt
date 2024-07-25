package io.github.bindglam.weirdcosmetic.players

import io.github.bindglam.weirdcosmetic.NPC
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player

class ClosetPlayer(val player: Player) {
    var cameraStand: ArmorStand? = null
    var mannequin: NPC? = null
}