package de.miskynet.arsenic;

import de.miskynet.arsenic.commands.openInventory;
import de.miskynet.arsenic.utils.CustomConfigs;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static final String missingString = "nullString";

    @Override
    public void onEnable() {

        // Load the configs
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        CustomConfigs.setup("shop");
        CustomConfigs.save("shop");

        getCommand("shop").setExecutor(new openInventory());
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
}
