package io.github.bindglam.weirdcosmetic.commands

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.EnumWrappers
import com.comphenix.protocol.wrappers.Pair
import io.github.bindglam.weirdcosmetic.CosmeticManager
import io.github.bindglam.weirdcosmetic.NPC
import io.github.bindglam.weirdcosmetic.WeirdCosmetic
import io.github.bindglam.weirdcosmetic.players.CosmeticPlayer
import io.github.bindglam.weirdcosmetic.utils.DependType.ITEMSADDER
import io.github.bindglam.weirdcosmetic.utils.DependType.ORAXEN
import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.time.Duration
import java.util.*

class ClosetCommand : CommandExecutor {
    override fun onCommand(player: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if(player !is Player || !cmd.label.equals("closet", true)) return false
        val cosmeticPlayer = CosmeticManager.cosmeticPlayers[player.uniqueId]!!

        if(args.isEmpty() && !cosmeticPlayer.isCloset){
            cosmeticPlayer.isCloset = true

            val fadeEffectGlyph = when(WeirdCosmetic.DEPEND_TYPE){
                ITEMSADDER -> TODO()
                ORAXEN -> PlaceholderAPI.setPlaceholders(null, "%oraxen_fullscreen%")
            }
            player.showTitle(Title.title(Component.text(fadeEffectGlyph).color(NamedTextColor.BLACK), Component.empty(),
                Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(1), Duration.ofSeconds(1))))

            Bukkit.getScheduler().scheduleSyncDelayedTask(WeirdCosmetic.INSTANCE, {
                val cameraStand = CosmeticManager.cameraLoc!!.world.spawn(CosmeticManager.cameraLoc!!, ArmorStand::class.java)
                cameraStand.setGravity(false)
                cameraStand.isMarker = true
                cameraStand.isInvulnerable = true
                cameraStand.isInvisible = true

                val spectatePacket = PacketContainer(PacketType.Play.Server.CAMERA)
                spectatePacket.integers.write(0, cameraStand.entityId)
                WeirdCosmetic.PROTOCOL_MANAGER.sendServerPacket(player, spectatePacket)
                player.teleportAsync(cameraStand.location.clone())

                cosmeticPlayer.closetCamera = cameraStand

                val npc = NPC(ProtocolLibrary.getProtocolManager(), UUID.randomUUID(), player.name)
                npc.spawn(player, CosmeticManager.mannequinLoc)
                npc.setEquipment(listOf(
                    Pair(EnumWrappers.ItemSlot.MAINHAND, player.inventory.itemInMainHand),
                    Pair(EnumWrappers.ItemSlot.OFFHAND, player.inventory.itemInOffHand),
                    Pair(EnumWrappers.ItemSlot.HEAD, player.inventory.helmet ?: ItemStack(Material.AIR)),
                    Pair(EnumWrappers.ItemSlot.CHEST, player.inventory.chestplate ?: ItemStack(Material.AIR)),
                    Pair(EnumWrappers.ItemSlot.LEGS, player.inventory.leggings ?: ItemStack(Material.AIR)),
                    Pair(EnumWrappers.ItemSlot.FEET, player.inventory.boots ?: ItemStack(Material.AIR)),
                ))
                cosmeticPlayer.mannequin = npc

                player.gameMode = GameMode.SPECTATOR
            }, 20L)
        } else {
            when(args[0]){
                "setloc" -> {
                    if(args.size < 2){
                        player.sendMessage("알맞지 않은 사용법입니다.")
                        return false
                    }

                    when(args[1]){
                        "camera" -> {
                            CosmeticManager.cameraLoc = player.eyeLocation
                            player.sendMessage("성공적으로 카메라 위치를 설정했습니다.")
                        }
                        "mannequin" -> {
                            CosmeticManager.mannequinLoc = player.location
                            player.sendMessage("성공적으로 마네킹 위치를 설정했습니다.")
                        }
                    }
                }
            }
        }
        return true
    }
}