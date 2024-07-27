package io.github.bindglam.weirdcosmetic;

import com.comphenix.protocol.ProtocolManager;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class EntityInfoUpdate {

    private final ProtocolManager protocolManager;
    private final UUID uuid;
    private final String name;

    public EntityInfoUpdate(
            ProtocolManager protocolManager,
            UUID uuid,
            String name) {
        this.protocolManager = protocolManager;
        this.uuid = uuid;
        this.name = name;
    }

    public void playerInfoUpdate(Player player) {
        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        String[] texturesName = getSkin(player);
        TextureProperty textures = new TextureProperty("textures", texturesName[0], texturesName[1]);
        UserProfile profile = new UserProfile(uuid, name, List.of(textures));

        WrapperPlayServerPlayerInfo.PlayerData playerData = new WrapperPlayServerPlayerInfo.PlayerData(player.name(), profile, GameMode.CREATIVE, 0);

        WrapperPlayServerPlayerInfo npc = new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.ADD_PLAYER, playerData);

        user.sendPacket(npc);
    }

    private static String[] getSkin(Player player){
        EntityPlayer p = ((CraftPlayer) player).getHandle();
        GameProfile profile = p.fX();
        Property property = profile.getProperties().get("textures").iterator().next();
        String texture = property.value();
        String signature = property.signature();
        return new String[] {texture, signature};
    }
}
