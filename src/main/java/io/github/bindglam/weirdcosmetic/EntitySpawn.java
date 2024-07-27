package io.github.bindglam.weirdcosmetic;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import net.minecraft.network.protocol.game.*;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EntitySpawn {

    private ProtocolManager protocolManager;
    private UUID uuid;

    public EntitySpawn(
            ProtocolManager protocolManager,
            UUID uuid) {
        this.protocolManager = protocolManager;
        this.uuid = uuid;
    }

    public int spawnEntity(Player player, Location location) {
        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        int entityID = -1;

        WrapperPlayServerSpawnEntity npc = new WrapperPlayServerSpawnEntity(entityID, Optional.of(uuid), EntityTypes.PLAYER,
                new Vector3d(location.getX(), location.getY(), location.getZ()), location.getPitch(), location.getYaw(), location.getYaw(), 0, Optional.of(Vector3d.zero()));

        user.sendPacket(npc);
        return entityID;
    }

    public void setEquipment(Player player, int entityID, List<Equipment> equipment){
        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        WrapperPlayServerEntityEquipment equipmentPacket = new WrapperPlayServerEntityEquipment(entityID, equipment);

        user.sendPacket(equipmentPacket);
    }

    public void updateRotation(Player player, float yaw, float pitch, int entityID){
        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        WrapperPlayServerEntityHeadLook headPacket = new WrapperPlayServerEntityHeadLook(entityID, yaw);
        WrapperPlayServerEntityRotation rotPacket = new WrapperPlayServerEntityRotation(entityID, yaw, pitch, true);

        user.sendPacket(headPacket);
        user.sendPacket(rotPacket);
    }

    public void removeEntity(Player player, int entityID) {
        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        WrapperPlayServerDestroyEntities removePacket = new WrapperPlayServerDestroyEntities(entityID);

        user.sendPacket(removePacket);
    }

}