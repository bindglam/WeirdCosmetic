package io.github.bindglam.weirdcosmetic.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.*;

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
        PacketContainer npc = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO); // make player info packet
        Set<EnumWrappers.PlayerInfoAction> playerInfoActionSet = new HashSet<>(); //The set collection that contains Player Info Action, very important

        /*
        Create a new custom game profile for our NPC.
        In the first arguments put a unique uuid generated with UUID.randomUUID();
        In the second arguments put a NPC's name above head
         */
        WrappedGameProfile wrappedGameProfile = new WrappedGameProfile(uuid, name);

        /*
        Create a new property about player's skin

        In the first leave as is and don't touch or nothing works thuth.
        In the second and third, indicate the values of your needs. how to get this data?

        Go to https://minecraftuuid.com/ and find the skin you want.
        If your found the skin, copy his Player UUID.

        Type a search in your browser, but don't press Enter, you'll need to edit that URL;
        https://sessionserver.mojang.com/session/minecraft/profile/uuid?unsigned=false

        Delete a word "uuid" and paste the player's uuid that you copied.
        And now you need to press Enter and you will get the result

        Copy the value and paste to second constructor arguments.
        Copy the signature and paster to third constructor arguments.

         */
        String[] name = getSkin(player);
        WrappedSignedProperty property = new WrappedSignedProperty(
                "textures",
                name[0],
                name[1]);

        //Now we need to add a skin property for our game profile.
        wrappedGameProfile.getProperties().clear();
        wrappedGameProfile.getProperties()
                .put("textures", property);

        /*
        In the first constructor arguments put our game profile
        In the second constructor arguments put your latency that your need
        In the third constructor arguments put a npc game mode, I put a creative.
        In the fourth constructor argument we need to put a wrapped chat component,
        I'm not lying and telling the truth - I don't know what it is. I think it's something to do with the chat,
        maybe it's the player name that is displayed if a player tell something in chat. I don't know, sorry.
         */
        PlayerInfoData playerInfoData = new PlayerInfoData(
                wrappedGameProfile,
                0,
                EnumWrappers.NativeGameMode.CREATIVE,
                WrappedChatComponent.fromText("name"));


        List<PlayerInfoData> playerInfoDataList = List.of(playerInfoData); //Add player's info data in list like here.

        playerInfoActionSet.add(EnumWrappers.PlayerInfoAction.ADD_PLAYER); //Adds player actions that the server must perform, in our case this is adding a player

        npc.getPlayerInfoActions()
                .write(0, playerInfoActionSet); //Add a player's action to our packet

        npc.getPlayerInfoDataLists().write(1, playerInfoDataList); //Add a list of player's info data in out packet

        /*
        In the first method arguments, we need to put player to who that send packet
        In the second method arguments, we need to put packet that sends.
         */
        protocolManager.sendServerPacket(player, npc);
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