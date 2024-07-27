package io.github.bindglam.weirdcosmetic.cosmetics

import io.github.bindglam.weirdcosmetic.CosmeticManager
import io.github.bindglam.weirdcosmetic.WeirdCosmetic
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object CosmeticLoader {
    private val cosmeticsFolder = File("plugins/WeirdCosmetic/cosmetics")

    fun load(){
        if(!cosmeticsFolder.exists())
            cosmeticsFolder.mkdirs()

        for(configFile in cosmeticsFolder.listFiles()!!){
            val config = YamlConfiguration.loadConfiguration(configFile)

            for(name in config.getKeys(false)){
                val section = config.getConfigurationSection(name)!!
                val keys = section.getKeys(true)

                var itemID = ""
                var clazz: Class<out AbstractCosmetic>? = null

                for(key in keys){
                    when(key){
                        "item_id" -> itemID = section.getString(key)!!
                        "class" -> clazz = CosmeticManager.cosmeticClasses[section.getString(key)!!]!!
                    }
                }

                val cosmetic = clazz!!.getDeclaredConstructor(String::class.java, String::class.java).newInstance(name, itemID)

                for(key in keys){
                    when(key){
                        "item_id", "class" -> continue
                        else -> {
                            cosmetic::class.java.getDeclaredField(key).apply {
                                isAccessible = true
                            }.set(cosmetic, section.get(key))
                        }
                    }
                }

                Bukkit.getPluginManager().registerEvents(cosmetic, WeirdCosmetic.INSTANCE)
                CosmeticManager.cosmetics[name] = cosmetic
            }
        }
    }
}