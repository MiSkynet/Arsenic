package de.miskynet.arsenic.listeners;

import de.miskynet.arsenic.Main;
import de.miskynet.arsenic.utils.CustomConfigs;
import de.miskynet.arsenic.utils.InventoryHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class inventoryClickEvent implements Listener {

    @EventHandler
    public static void onInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        if (event.getView().getTitle().equals(CustomConfigs.get("shop").getString("title"))) {

            for (String key : CustomConfigs.get("shop").getConfigurationSection("items").getKeys(false)) {

                if (event.isLeftClick()) {
                    // Search for the clicked item in the config
                    if (event.getCurrentItem().getItemMeta().getDisplayName().equals(Main.replaceString(CustomConfigs.get("shop").getString("items." + key + ".displayName"), key))) {

                        // Check if the player has enough money
                        if (Main.econ.getBalance(player) >= CustomConfigs.get("shop").getDouble("items." + key + ".price.buy")) {

                            // Remove the money from the player
                            Main.econ.withdrawPlayer(player, CustomConfigs.get("shop").getDouble("items." + key + ".price.buy"));

                            // Give the player the item
                            player.getInventory().addItem(InventoryHelper.createItemStack(key));
                        } else {
                            player.sendMessage("You don't have enough money to buy this item.");
                        }

                    }
                }
            }
            event.setCancelled(true);
        }
    }
}
