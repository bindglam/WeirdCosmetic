package io.github.bindglam.weirdcosmetic;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
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

        /*
        Create a named entity spawn packet
         */
        PacketContainer npc = protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        int entityID = -1;

        npc.getIntegers()
                .write(0, entityID) //the second value it's entity id, it must be unique!
                .writeSafely(1, 122); //the second value it's same entity id, but it use for only spawn entity

        npc.getUUIDs()
                .write(0, uuid); //uuid must be like uuid in player info packet!

        npc.getEntityTypeModifier()
                .writeSafely(0, EntityType.PLAYER); //Entity Type, nothing complicated.

        npc.getDoubles()
                .write(0, location.getX())
                .write(1, location.getY())
                .write(2, location.getZ()); //Spawn location

        npc.getBytes()
                .write(0, (byte) (location.getYaw()/(256.0F / 360.0F)))
                .write(1, (byte) (location.getPitch()/(256.0F / 360.0F))); //rotate location

        /*
        In the first method arguments, we need to put player to who that send packet
        In the second method arguments, we need to put packet that sends.
         */
        protocolManager.sendServerPacket(player, npc);
        return entityID;
    }

    public void setEquipment(Player player, int entityID, List<Pair<EnumWrappers.ItemSlot, ItemStack>> equipment){
        PacketContainer equipmentPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
        //PacketPlayOutEntityDestroy

        equipmentPacket.getIntegers().write(0, entityID);
        equipmentPacket.getSlotStackPairLists().write(0, equipment);

        protocolManager.sendServerPacket(player, equipmentPacket);
    }

    public void updateRotation(Player player, float yaw, float pitch, int entityID){
        PacketContainer rotPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_LOOK);
        PacketContainer headPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
        //PacketPlayOutEntity.PacketPlayOutEntityLook

        rotPacket.getIntegers().write(0, entityID);
        headPacket.getIntegers().write(0, entityID);
        rotPacket.getBytes().write(0, (byte) (yaw/(256.0F / 360.0F)));
        headPacket.getBytes().write(0, (byte) (yaw/(256.0F / 360.0F)));
        rotPacket.getBytes().write(1, (byte) (pitch/(256.0F / 360.0F)));
        rotPacket.getBooleans().write(0, true);

        protocolManager.sendServerPacket(player, rotPacket);
        protocolManager.sendServerPacket(player, headPacket);
    }

    public void removeEntity(Player player, int entityID) {
        PacketContainer removePacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        //PacketPlayOutEntityDestroy

        removePacket.getIntLists().write(0, List.of(entityID));

        protocolManager.sendServerPacket(player, removePacket);
    }

}