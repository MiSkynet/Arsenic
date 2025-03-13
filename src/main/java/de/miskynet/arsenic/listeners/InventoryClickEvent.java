package de.miskynet.arsenic.listeners;

import de.miskynet.arsenic.Main;
import de.miskynet.arsenic.utils.CustomConfigs;
import de.miskynet.arsenic.utils.InventoryHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class InventoryClickEvent implements Listener {

    @EventHandler
    public static void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        // Check if the player clicked an item
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == null) {
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
                    if (event.getCurrentItem().getItemMeta().getDisplayName().equals(InventoryHelper.replaceString("shop", CustomConfigs.get("shop").getString("items." + key + ".displayName"), key))) {

                        // Check if the clicked item is in the shop
                        if (event.getRawSlot() <= (event.getInventory().getSize() - 1)) {
                            player.openInventory(InventoryHelper.setupBuyInventory(key));
                        }
                    }
                }
            }
            event.setCancelled(true);
        } else if (event.getView().getTitle().equals(buyMenu)) {

            // Loop through all items in the config
            for (String key : CustomConfigs.get("buyMenu").getConfigurationSection("items").getKeys(false)) {

                // Check if the player clicked the left mouse button
                if (!event.isLeftClick()) {
                    event.setCancelled(true);
                    break;
                }

                // Check if the clicked item is in the shop
                if (!(event.getRawSlot() <= (event.getInventory().getSize() - 1))) {
                    event.setCancelled(true);
                    break;
                }

                int clickedSlot = event.getSlot();
                int configSlot = CustomConfigs.get("buyMenu").getInt("items." + key + ".slot") - 1;

                // Check if the slot a player clicked on is the slot of an item in the config
                if (clickedSlot == configSlot) {
                    // Check if the player has enough money
                    if (Main.econ.getBalance(player) >= CustomConfigs.get("shop").getDouble("items." + key + ".price.buy")) {

                        ItemStack itemToBuy = event.getInventory().getItem(CustomConfigs.get("buyMenu").getInt("clickedItemSlot"));
                        Integer calculateAmount = CustomConfigs.get("buyMenu").getInt("items." + key + ".itemData.amount");

                        itemToBuy.setAmount(calculateAmount);

                        if (!InventoryHelper.canAddItem(player, itemToBuy)) {
                            player.sendMessage("§cYou don't have enough space in your inventory to buy this item.");
                            event.setCancelled(true);
                            break;
                        }

                        player.getInventory().addItem(itemToBuy);

                        Main.econ.withdrawPlayer(player, CustomConfigs.get("shop").getDouble("items." + key + ".price.buy"));
                        break;
                    } else {
                        player.sendMessage("§cYou don't have enough money to buy this item.");
                        event.setCancelled(true);
                        break;
                    }
                }
            }
            event.setCancelled(true);
        }
    }
}
