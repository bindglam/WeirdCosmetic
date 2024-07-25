package io.github.bindglam.weirdcosmetic.listeners

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import io.github.bindglam.weirdcosmetic.ClosetManager
import io.github.bindglam.weirdcosmetic.WeirdCosmetic
import io.github.bindglam.weirdcosmetic.guis.ClosetGui
import io.github.bindglam.weirdcosmetic.utils.DependType
import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import java.time.Duration
import kotlin.math.cos
import kotlin.math.sin

class PlayerListener : Listener {
    @EventHandler
    fun onMove(event: PlayerMoveEvent){
        val player = event.player
        if(!ClosetManager.closets.containsKey(player.uniqueId)) return
        val closetPlayer = ClosetManager.closets[player.uniqueId]!!

        event.isCancelled = true
        player.teleportAsync(closetPlayer.cameraStand!!.location)
    }

    @EventHandler
    fun onSneak(event: PlayerToggleSneakEvent){
        val player = event.player
        if(!ClosetManager.closets.containsKey(player.uniqueId)) return
        val closetPlayer = ClosetManager.closets[player.uniqueId]!!

        ClosetManager.closets.remove(player.uniqueId)

        val fadeEffectGlyph = when(WeirdCosmetic.DEPEND_TYPE){
            DependType.ITEMSADDER -> TODO()
            DependType.ORAXEN -> PlaceholderAPI.setPlaceholders(null, "%oraxen_fullscreen%")
        }
        player.showTitle(Title.title(Component.text(fadeEffectGlyph).color(NamedTextColor.BLACK), Component.empty(),
            Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(1), Duration.ofSeconds(1))))

        Bukkit.getScheduler().scheduleSyncDelayedTask(WeirdCosmetic.INSTANCE, {
            val spectatePacket = PacketContainer(PacketType.Play.Server.CAMERA)
            spectatePacket.integers.write(0, player.entityId)
            WeirdCosmetic.PROTOCOL_MANAGER.sendServerPacket(player, spectatePacket)

            player.teleportAsync(closetPlayer.cameraStand!!.location)
            player.gameMode = GameMode.SURVIVAL

            closetPlayer.cameraStand!!.remove()
            closetPlayer.mannequin!!.spawner.removeEntity(player, closetPlayer.mannequin!!.entityID)
        }, 20L)
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent){
        val player = event.player
        if(!ClosetManager.closets.containsKey(player.uniqueId)) return
        val closetPlayer = ClosetManager.closets[player.uniqueId]!!

        ClosetGui(0).open(player)
    }
}