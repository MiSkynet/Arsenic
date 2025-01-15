package de.miskynet.arsenic;

import de.miskynet.arsenic.utils.CustomConfig;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        // Load the config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        CustomConfig.setup("pages");

        getCommand("openInventory").setExecutor(new de.miskynet.arsenic.commands.openInventory());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}
