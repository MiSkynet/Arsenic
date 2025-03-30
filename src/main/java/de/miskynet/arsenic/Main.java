package de.miskynet.arsenic;

import de.miskynet.arsenic.commands.ArsenicTabCompleter;
import de.miskynet.arsenic.commands.OpenInventory;
import de.miskynet.arsenic.commands.Arsenic;
import de.miskynet.arsenic.listeners.InventoryClickEvent;
import de.miskynet.arsenic.utils.CreateItems;
import de.miskynet.arsenic.utils.CustomConfigs;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;


public final class Main extends JavaPlugin {

    public static final String missingString = "nullString";
    public static Economy econ;
    public static HashMap<String, ItemStack> itemsShopMenu = new HashMap<>();
    public static HashMap<String, ItemStack> itemsBuyMenu = new HashMap<>();
    public static HashMap<UUID, String> currentPlayerKey = new HashMap<>();

    @Override
    public void onEnable() {

        // load the configs
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        CustomConfigs.setup("shop");
        CustomConfigs.save("shop");
        CustomConfigs.setup("buyMenu");
        CustomConfigs.save("buyMenu");

        // set up the economy
        if (!setupEconomy() ) {
            getLogger().config("Disabled due to no Vault dependency found");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // setup all items
        loadAllItems();

        // register the commands
        getCommand("shop").setExecutor(new OpenInventory());
        getCommand("arsenic").setExecutor(new Arsenic());
        getCommand("arsenic").setTabCompleter(new ArsenicTabCompleter());

        // register the events
        getServer().getPluginManager().registerEvents(new InventoryClickEvent(), this);
    }

    // get the plugin instance
    public static Main getInstance() {
        return getPlugin(Main.class);
    }

    // set up the economy
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

    // get the amount of items in a players inventory
    public static Integer getItemAmount(Player player, ItemStack itemStack) {
        Integer itemAmountInInventory = 0;

        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.isSimilar(itemStack)) {
                itemAmountInInventory += item.getAmount();
            }
        }

        return itemAmountInInventory;
    }

    // load all the items
    public static void loadAllItems() {

        for (String key : CustomConfigs.get("shop").getConfigurationSection("items").getKeys(false)) {

            ItemStack itemStack = CreateItems.createItemStackFromConfig("shop", key, true);

            itemsShopMenu.put(key, itemStack);
        }

        for (String key : CustomConfigs.get("buyMenu").getConfigurationSection("items").getKeys(false)) {

            ItemStack itemStack = CreateItems.createItemStackFromConfig("buyMenu", key, true);

            itemsBuyMenu.put(key, itemStack);
        }

    }
 }
