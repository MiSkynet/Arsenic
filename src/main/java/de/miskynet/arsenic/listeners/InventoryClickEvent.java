package de.miskynet.arsenic.listeners;

import de.miskynet.arsenic.Main;
import de.miskynet.arsenic.utils.CustomConfigs;
import de.miskynet.arsenic.utils.InventoryHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class InventoryClickEvent implements Listener {

    @EventHandler
    public static void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        // Check if the player clicked in the shop
        if (!event.getView().getTitle().equals(CustomConfigs.get("shop").getString("title"))) {
            return;
        }

        if (event.getCurrentItem() == null) {
            event.setCancelled(true);
            return;
        }

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

                            Bukkit.getLogger().info("4");

                            player.openInventory(InventoryHelper.setupBuyInventory(key));

//                        // Remove the money from the player
//                        Main.econ.withdrawPlayer(player, CustomConfigs.get("shop").getDouble("items." + key + ".price.buy"));
//
//                        // Give the player the item
//                        player.getInventory().addItem(InventoryHelper.createItemStackFromConfig(key));
//
//                        // Send the player a message
//                        player.sendMessage("§aYour current balance is: " + Main.econ.format(Main.econ.getBalance(player)));
                        } else {
                            player.sendMessage("§cYou don't have enough money to buy this item.");
                        }
                    }
                }

            }
            event.setCancelled(true);

        }
    }
}
