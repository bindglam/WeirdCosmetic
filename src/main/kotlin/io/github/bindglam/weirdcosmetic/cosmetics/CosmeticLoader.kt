package io.github.bindglam.weirdcosmetic.cosmetics

import io.github.bindglam.weirdcosmetic.ClosetManager
import io.github.bindglam.weirdcosmetic.WeirdCosmetic
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object CosmeticLoader {
    private val cosmeticsFolder = File("plugins/WeirdCosmetic/cosmetics")
    private val cosmeticClasses = ArrayList<Class<out AbstractCosmetic>>()

    fun load(){
        if(!cosmeticsFolder.exists())
            cosmeticsFolder.mkdirs()

        for(configFile in cosmeticsFolder.listFiles()!!){
            val config = YamlConfiguration.loadConfiguration(configFile)

            var itemID = ""
            var className = ""

            for(sectionName in config.getKeys(true)){
                if(sectionName.split(".").size <= 1) continue
                when(sectionName.split(".")[1]){
                    "item_id" -> itemID = config.getString(sectionName)!!
                    "class" -> className = config.getString(sectionName)!!
                }
            }

            var cosmetic: AbstractCosmetic? = null
            for(clazz in cosmeticClasses){
                if(clazz.simpleName == className){
                    cosmetic = clazz.getDeclaredConstructor(String::class.java).newInstance(itemID)
                }
            }
            if(cosmetic != null)
                ClosetManager.cosmetics[itemID] = cosmetic
            else
                throw IllegalStateException("Cosmetic is not initalized!")
        }
    }

    fun register(cosmetic: Class<out AbstractCosmetic>){
        WeirdCosmetic.INSTANCE.logger.info("${cosmetic.simpleName} is registered!")
        cosmeticClasses.add(cosmetic)
    }
}