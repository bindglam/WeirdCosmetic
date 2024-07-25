package io.github.bindglam.weirdcosmetic

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import io.github.bindglam.weirdcosmetic.commands.ClosetCommand
import io.github.bindglam.weirdcosmetic.cosmetics.CosmeticLoader
import io.github.bindglam.weirdcosmetic.cosmetics.HatCosmetic
import io.github.bindglam.weirdcosmetic.listeners.PlayerListener
import io.github.bindglam.weirdcosmetic.listeners.ServerListener
import io.github.bindglam.weirdcosmetic.utils.DependType
import org.bukkit.plugin.java.JavaPlugin

class WeirdCosmetic : JavaPlugin() {
    override fun onEnable() {
        INSTANCE = this
        PROTOCOL_MANAGER = ProtocolLibrary.getProtocolManager()

        if(server.pluginManager.isPluginEnabled("Oraxen")){
            DEPEND_TYPE = DependType.ORAXEN
        } else if(server.pluginManager.isPluginEnabled("ItemsAdder")){
            DEPEND_TYPE = DependType.ITEMSADDER
        } else {
            logger.severe("Required plugins not found!")
            server.pluginManager.disablePlugin(this)
        }

        getCommand("closet")?.setExecutor(ClosetCommand())

        server.pluginManager.registerEvents(PlayerListener(), this)
        server.pluginManager.registerEvents(ServerListener(), this)

        CosmeticLoader.register(HatCosmetic::class.java)

        ClosetManager.load()
        CosmeticLoader.load()

        saveDefaultConfig()
    }

    override fun onDisable() {
        ClosetManager.save()

        saveConfig()
    }

    companion object{
        lateinit var INSTANCE: WeirdCosmetic
        lateinit var PROTOCOL_MANAGER: ProtocolManager
        lateinit var DEPEND_TYPE: DependType
    }
}