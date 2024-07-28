package io.github.bindglam.weirdcosmetic.packet;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class NPC {
    private final ProtocolManager protocolManager;
    private final UUID uuid;
    private final String name;
    private int entityID;
    private Player player;

    private EntityPacket spawner;

    private float yaw, pitch;

    private HashMap<EnumWrappers.ItemSlot, ItemStack> equipment = new HashMap<>();

    public NPC(
            ProtocolManager protocolManager,
            UUID uuid,
            String name) {
        this.protocolManager = protocolManager;
        this.uuid = uuid;
        this.name = name;
    }

    public void spawn(Player player, Location location) {
        EntityInfoUpdate updateInfo = new EntityInfoUpdate(protocolManager, uuid, name);
        spawner = new EntityPacket(protocolManager, uuid);

        this.player = player;
        yaw = location.getYaw();
        pitch = location.getPitch();

        updateInfo.playerInfoUpdate(player);
        entityID = spawner.spawnEntity(player, location);
    }

    public HashMap<EnumWrappers.ItemSlot, ItemStack> getEquipment() {
        return equipment;
    }

    public void setEquipment(HashMap<EnumWrappers.ItemSlot, ItemStack> equipment){
        this.equipment = equipment;

        List<Pair<EnumWrappers.ItemSlot, ItemStack>> result = new ArrayList<>();
        for(EnumWrappers.ItemSlot slot : equipment.keySet()){
            result.add(new Pair<>(slot, equipment.get(slot)));
        }
        spawner.setEquipment(player, entityID, result);
    }

    public float getYaw(){
        return yaw;
    }

    public void setYaw(float yaw){
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public EntityPacket getSpawner() {
        return spawner;
    }

    public int getEntityID() {
        return entityID;
    }
}
