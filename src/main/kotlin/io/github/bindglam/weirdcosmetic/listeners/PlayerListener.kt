package io.github.bindglam.weirdcosmetic.listeners

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.EnumWrappers
import com.comphenix.protocol.wrappers.Pair
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import io.github.bindglam.weirdcosmetic.CosmeticManager
import io.github.bindglam.weirdcosmetic.NPC
import io.github.bindglam.weirdcosmetic.WeirdCosmetic
import io.github.bindglam.weirdcosmetic.guis.ClosetGui
import io.github.bindglam.weirdcosmetic.players.CosmeticPlayer
import io.github.bindglam.weirdcosmetic.utils.DependType
import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.ItemStack
import java.time.Duration
import java.util.*
import kotlin.math.cos

class PlayerListener : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent){
        val player = event.player

        CosmeticManager.cosmeticPlayers[player.uniqueId] = CosmeticPlayer(player)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent){
        val player = event.player

        CosmeticManager.cosmeticPlayers.remove(player.uniqueId)
    }

    @EventHandler
    fun onSneak(event: PlayerToggleSneakEvent){
        val player = event.player
        val cosmeticPlayer = CosmeticManager.cosmeticPlayers[player.uniqueId]!!

        if(cosmeticPlayer.isCloset){
            cosmeticPlayer.isCloset = false

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

                cosmeticPlayer.mannequin!!.spawner.removeEntity(player, cosmeticPlayer.mannequin!!.entityID)
                cosmeticPlayer.closetCamera!!.remove()

                player.teleportAsync(CosmeticManager.cameraLoc!!)
                player.gameMode = GameMode.SURVIVAL
            }, 20L)
        }
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent){
        val player = event.player
        val cosmeticPlayer = CosmeticManager.cosmeticPlayers[player.uniqueId]!!

        if(cosmeticPlayer.isCloset){
            ClosetGui(0).open(player)
        }
    }
}