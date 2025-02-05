package de.miskynet.arsenic;

import de.miskynet.arsenic.utils.CustomConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;

public final class Main extends JavaPlugin {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    private String title;
    private int size;

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("[Arsenic] ");
        Bukkit.getLogger().info("[Arsenic] Plugin loading...");
        // Plugin startup logic
        instance = this;

        // Load the config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        CustomConfig.setup("pages");
        CustomConfig.save("pages");

        setTitle();
        setSize();

        getServer().getPluginManager().registerEvents(new de.miskynet.arsenic.listener.Listener(), this);

        getCommand("openInventory").setExecutor(new de.miskynet.arsenic.commands.openInventory());

        Bukkit.getLogger().info("[Arsenic] Plugin loaded");
        Bukkit.getLogger().info("[Arsenic] ");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    // Set the title of the inventory
    public String setTitle() {
        String configTitle = CustomConfig.get("pages").getString("title");
        if (configTitle != null) {
            title = configTitle;
            title = ChatColor.translateAlternateColorCodes('&', title);
        }else {
            Bukkit.getLogger().warning("[Arsenic] Invalid title in config file 'pages.yml'. Defaulting to 'Shop Inventory'.");
            title = "Shop Inventory";
        }
        return title;
    }

    // Set the size of the inventory
    public int setSize() {
        ArrayList<Integer> sizes = new ArrayList<>(Arrays.asList(9, 18, 27, 36, 45, 54));
        int configSize = CustomConfig.get("pages").getInt("size", 54);

        if (sizes.contains(configSize)) {
            size = CustomConfig.get("pages").getInt("size");
        }else {
            Bukkit.getLogger().warning("[Arsenic] Invalid size in config file 'pages.yml'. Defaulting to 54.");
            size = 54;
        }
        return size;
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }
}
