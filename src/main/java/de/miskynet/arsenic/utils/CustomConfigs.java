package de.miskynet.arsenic.utils;

import de.miskynet.arsenic.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CustomConfigs {

    private static File file;
    private static FileConfiguration customFile;

    // Set up the file
    public static void setup(String filename){
        file = new File(Bukkit.getServer().getPluginManager().getPlugin(Main.getInstance().getName()).getDataFolder(), filename + ".yml");

        if (!file.exists()){
            try{
                file.getParentFile().mkdirs();
                Bukkit.getServer().getPluginManager().getPlugin(Main.getInstance().getName()).saveResource(filename + ".yml", true);
            }catch (NullPointerException e){
                Bukkit.getLogger().config("Couldn't create file");
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    // Get the file
    public static FileConfiguration get(String fileName){
        file = new File(Bukkit.getServer().getPluginManager().getPlugin(Main.getInstance().getName()).getDataFolder(), fileName + ".yml");
        customFile = YamlConfiguration.loadConfiguration(file);
        return customFile;
    }

    // Save the file
    public static void save(String fileName){
        try{
            file = new File(Bukkit.getServer().getPluginManager().getPlugin(Main.getInstance().getName()).getDataFolder(), fileName + ".yml");
            customFile = YamlConfiguration.loadConfiguration(file);
            customFile.save(file);
        }catch (IOException e){
            Bukkit.getLogger().config("Couldn't save file");
        }
    }

    // Reload the file
    public static void reload(String fileName){
        file = new File(Bukkit.getServer().getPluginManager().getPlugin(Main.getInstance().getName()).getDataFolder(), fileName + ".yml");
        customFile = YamlConfiguration.loadConfiguration(file);
    }

}
