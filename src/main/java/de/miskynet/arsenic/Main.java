package de.miskynet.arsenic;

import de.miskynet.arsenic.commands.OpenInventory;
import de.miskynet.arsenic.listeners.InventoryClickEvent;
import de.miskynet.arsenic.utils.CustomConfigs;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static final String missingString = "nullString";
    public static Economy econ;

    @Override
    public void onEnable() {

        // Load the configs
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        CustomConfigs.setup("shop");
        CustomConfigs.save("shop");
        CustomConfigs.setup("buyMenu");
        CustomConfigs.save("buyMenu");

        if (!setupEconomy() ) {
            getLogger().config("Disabled due to no Vault dependency found");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Register the commands
        getCommand("shop").setExecutor(new OpenInventory());

        // Register the events
        getServer().getPluginManager().registerEvents(new InventoryClickEvent(), this);
    }

    // Get the plugin instance
    public static Main getInstance() {
        return getPlugin(Main.class);
    }

    // Set up the economy
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

    public static Integer getItemAmount(Player player, ItemStack itemStack) {

        Integer itemAmountInInventory = 0;

        for (int i = 0; i < player.getInventory().getSize(); i++) {

            if (player.getInventory().getItem(i) != null) {
                if (player.getInventory().getItem(i).isSimilar(itemStack)) {
                    itemAmountInInventory += player.getInventory().getItem(i).getAmount();
                }
            }
        }

        return itemAmountInInventory;

    }

 }
