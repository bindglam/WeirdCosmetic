package io.github.bindglam.weirdcosmetic.cosmetics

import io.github.bindglam.weirdcosmetic.CosmeticManager
import io.github.bindglam.weirdcosmetic.players.CosmeticPlayer
import org.bukkit.entity.ItemDisplay
import org.bukkit.util.Transformation
import org.joml.AxisAngle4f
import org.joml.Vector3f
import java.util.UUID

class BackpackCosmetic(cosmeticID: String, itemID: String) : AbstractCosmetic(cosmeticID, itemID) {
    val backpacks = HashMap<UUID, List<ItemDisplay>>()

    var offsetX = 0.0
    var offsetY = 0.0
    var offsetZ = 0.0

    override fun onEquip(cosmeticPlayer: CosmeticPlayer) {
        val display = cosmeticPlayer.player.world.spawn(cosmeticPlayer.player.location, ItemDisplay::class.java)
        val display2 = cosmeticPlayer.player.world.spawn(CosmeticManager.mannequinLoc!!.clone().add(0.0, cosmeticPlayer.player.eyeHeight, 0.0), ItemDisplay::class.java)
        display.setItemStack(itemStack)
        display2.setItemStack(itemStack)

        cosmeticPlayer.player.addPassenger(display)
        //cosmeticPlayer.mannequin!!.spawner.addPassenger(cosmeticPlayer.player, cosmeticPlayer.mannequin!!.entityID, display2)
        backpacks[cosmeticPlayer.player.uniqueId] = listOf(display, display2)
    }

    override fun onTick(cosmeticPlayer: CosmeticPlayer) {
        val displays = backpacks[cosmeticPlayer.player.uniqueId]!!

        displays[0].transformation = Transformation(
            Vector3f(offsetX.toFloat(), offsetY.toFloat(), offsetZ.toFloat()),
            AxisAngle4f(),
            Vector3f(1f, 1f, 1f),
            AxisAngle4f(-Math.toRadians(cosmeticPlayer.player.bodyYaw.toDouble()).toFloat(), 0f, 1f, 0f)
        )

        if(cosmeticPlayer.isCloset){
            displays[1].transformation = Transformation(
                Vector3f(offsetX.toFloat(), offsetY.toFloat(), offsetZ.toFloat()),
                AxisAngle4f(),
                Vector3f(1f, 1f, 1f),
                AxisAngle4f(-Math.toRadians(cosmeticPlayer.mannequin!!.yaw.toDouble()).toFloat(), 0f, 1f, 0f)
            )
        } else {
            displays[1].remove()
        }
        //display.setRotation(cosmeticPlayer.player.bodyYaw, 0f)
    }

    override fun onUnequip(cosmeticPlayer: CosmeticPlayer) {
        val displays = backpacks[cosmeticPlayer.player.uniqueId]!!
        cosmeticPlayer.player.removePassenger(displays[0])
        displays[0].remove()
        displays[1].remove()
        backpacks.remove(cosmeticPlayer.player.uniqueId)
    }
}