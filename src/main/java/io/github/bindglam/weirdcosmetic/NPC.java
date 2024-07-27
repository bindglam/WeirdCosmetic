package io.github.bindglam.weirdcosmetic;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NPC {

    private final ProtocolManager protocolManager;
    private final UUID uuid;
    private final String name;
    private int entityID;
    private Player player;

    private EntitySpawn spawner;

    private float yaw, pitch;

    private List<Equipment> equipment = new ArrayList<>();

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
        spawner = new EntitySpawn(protocolManager, uuid);

        this.player = player;
        yaw = location.getYaw();
        pitch = location.getPitch();

        updateInfo.playerInfoUpdate(player);
        entityID = spawner.spawnEntity(player, location);
    }

    public List<Equipment> getEquipment(){
        return equipment;
    }

    public void setEquipment(List<Equipment> equipment){
        this.equipment = equipment;
        spawner.setEquipment(player, entityID, equipment);
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

    public EntitySpawn getSpawner() {
        return spawner;
    }

    public int getEntityID() {
        return entityID;
    }
}