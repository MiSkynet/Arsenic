package de.miskynet.arsenic;

import de.miskynet.arsenic.commands.openInventory;
import de.miskynet.arsenic.listeners.inventoryClickEvent;
import de.miskynet.arsenic.utils.CustomConfigs;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static final String missingString = "nullString";

    public static Economy econ = null;

    @Override
    public void onEnable() {

        // Load the configs
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        CustomConfigs.setup("shop");
        CustomConfigs.save("shop");
        getLogger().info("Configs loaded");

        if (!setupEconomy() ) {
            getLogger().config("Disabled due to no Vault dependency found");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }else {
            getLogger().info("Vault dependency found!");
        }

        // Register the commands
        getCommand("shop").setExecutor(new openInventory());
        getLogger().info("Commands registered");

        // Register the events
        getServer().getPluginManager().registerEvents(new inventoryClickEvent(), this);
        getLogger().info("Events registered");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getInstance() {
        return getPlugin(Main.class);
    }

    public static String replaceString(String string, String key) {

        String priceSell = CustomConfigs.get("shop").getString("items." + key + ".price.sell");
        String priceBuy = CustomConfigs.get("shop").getString("items." + key + ".price.buy");

        if (priceSell != null) {
            string = string.replace("%priceSell%", priceSell);
        }

        if (priceBuy != null) {
            string = string.replace("%priceBuy%", priceBuy);
        }

        string = ChatColor.translateAlternateColorCodes('&', string);

        return string;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return true;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return true;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

}
