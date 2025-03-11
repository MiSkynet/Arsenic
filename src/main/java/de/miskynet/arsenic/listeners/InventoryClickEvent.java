package de.miskynet.arsenic.listeners;

import de.miskynet.arsenic.Main;
import de.miskynet.arsenic.utils.CustomConfigs;
import de.miskynet.arsenic.utils.InventoryHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class InventoryClickEvent implements Listener {

    @EventHandler
    public static void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        // Check if the player clicked an item
        if (event.getCurrentItem() == null) {
            event.setCancelled(true);
            return;
        }

        String mainShop = CustomConfigs.get("shop").getString("title");
        String buyMenu = CustomConfigs.get("buyMenu").getString("title");

        if (event.getView().getTitle().equals(mainShop)) {

            // Loop through all items in the config
            for (String key : CustomConfigs.get("shop").getConfigurationSection("items").getKeys(false)) {

                // Check if the player clicked the left mouse button
                if (event.isLeftClick()) {

                    // Search for the clicked item in the config
                    if (event.getCurrentItem().getItemMeta().getDisplayName().equals(Main.replaceString(CustomConfigs.get("shop").getString("items." + key + ".displayName"), key))) {

                        // Check if the clicked item is in the shop
                        if (event.getRawSlot() <= (event.getInventory().getSize() - 1)) {

                            // Check if the player has enough money
                            if (Main.econ.getBalance(player) >= CustomConfigs.get("shop").getDouble("items." + key + ".price.buy")) {

                                player.openInventory(InventoryHelper.setupBuyInventory(key));

                            } else {
                                player.sendMessage("§cYou don't have enough money to buy this item.");
                            }
                        }
                    }
                }
            }
            event.setCancelled(true);
        }else if (event.getView().getTitle().equals(buyMenu)) {

            // Loop through all items in the config
            for (String key : CustomConfigs.get("buyMenu").getConfigurationSection("items").getKeys(false)) {

                // Check if the player clicked the left mouse button
                if (event.isLeftClick()) {

                    // Search for the clicked item in the config
                    if (event.getCurrentItem().getItemMeta().getDisplayName().equals(Main.replaceString(CustomConfigs.get("shop").getString("items." + key + ".displayName"), key))) {

                        // Check if the clicked item is in the shop
                        if (event.getRawSlot() <= (event.getInventory().getSize() - 1)) {

                            // Check if the player has enough money
                            if (Main.econ.getBalance(player) >= CustomConfigs.get("shop").getDouble("items." + key + ".price.buy")) {

                                player.openInventory(InventoryHelper.setupBuyInventory(key));

                            } else {
                                player.sendMessage("§cYou don't have enough money to buy this item.");
                            }
                        }
                    }
                }
            }

        }
    }
}
