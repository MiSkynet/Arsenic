package de.miskynet.arsenic.utils;

import de.miskynet.arsenic.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CustomConfig {

    // Variables
    private static File file;
    private static FileConfiguration customFile;

    // Creates the file if it doesn't exist
    public static void setup(String fileName){
        file = new File(Bukkit.getServer().getPluginManager().getPlugin(Main.getInstance().getName()).getDataFolder(), fileName + ".yml");

        if (!file.exists()){
            try{
                file.createNewFile();
            }catch (IOException e){
                Bukkit.getLogger().warning("Couldn't create file: " + fileName);
                e.printStackTrace();
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    // Returns a specific file
    public static FileConfiguration get(String fileName){
        file = new File(Bukkit.getServer().getPluginManager().getPlugin(Main.getInstance().getName()).getDataFolder(), fileName + ".yml");
        customFile = YamlConfiguration.loadConfiguration(file);
        return customFile;
    }

    // Saves a specific file
    public static void save(String fileName){
        try{
            file = new File(Bukkit.getServer().getPluginManager().getPlugin(Main.getInstance().getName()).getDataFolder(), fileName + ".yml");
            customFile.save(file);
        }catch (IOException e){
            Bukkit.getLogger().warning("Couldn't save file: " + fileName + ".yml");
        }
    }

    // Reloads a specific file
    public static void reload(String fileName){
        file = new File(Bukkit.getServer().getPluginManager().getPlugin(Main.getInstance().getName()).getDataFolder(), fileName + ".yml");
        customFile = YamlConfiguration.loadConfiguration(file);
    }

}
