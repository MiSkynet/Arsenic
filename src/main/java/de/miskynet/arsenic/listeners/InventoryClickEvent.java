package de.miskynet.arsenic.listeners;

import de.miskynet.arsenic.Main;
import de.miskynet.arsenic.utils.CustomConfigs;
import de.miskynet.arsenic.utils.InventoryHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

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

            // Check if the player clicked the left mouse button
            if (!event.isLeftClick()) {
                event.setCancelled(true);
            }

            // Check if the clicked item is in the shop
            if (!(event.getRawSlot() <= (event.getInventory().getSize() - 1))) {
                event.setCancelled(true);
            }

            // Loop through all items in the config
            for (String key : CustomConfigs.get("buyMenu").getConfigurationSection("items").getKeys(false)) {

                int clickedSlot = event.getSlot();
                int configSlot = CustomConfigs.get("buyMenu").getInt("items." + key + ".slot") - 1;

                // Check if the slot a player clicked on is the slot of an item in the config
                if (clickedSlot == configSlot) {

                    Integer calculateAmount = CustomConfigs.get("buyMenu").getInt("items." + key + ".itemData.amount");
                    double itemPrice = 0.0;
                    double itemSellPrice = 0.0;

                    ItemStack itemToBuy = event.getInventory().getItem(CustomConfigs.get("buyMenu").getInt("clickedItemSlot"));
                    ItemStack purchasedItem = itemToBuy.clone();
                    ItemMeta purchasedItemMeta = purchasedItem.getItemMeta();
                    purchasedItem.setAmount(calculateAmount);

                    // Modify the lore of the item
                    for (String purchasedItemKey : CustomConfigs.get("shop").getConfigurationSection("items").getKeys(false)) {

                        // Check if the item Displayname is the same as the item in the config
                        if (itemToBuy.getItemMeta().getDisplayName().equals(InventoryHelper.replaceString("shop", CustomConfigs.get("shop").getString("items." + purchasedItemKey + ".displayName"), purchasedItemKey))) {

                            itemPrice = CustomConfigs.get("shop").getDouble("items." + purchasedItemKey + ".price.buy");

                            itemSellPrice = CustomConfigs.get("shop").getDouble("items." + purchasedItemKey + ".price.sell");

                            purchasedItemMeta.setLore(CustomConfigs.get("shop").getStringList(""));

                            List<String> lore = CustomConfigs.get("shop").getStringList("items." + purchasedItemKey + ".lore");

                            for (int i = 0; i < lore.size(); i++) {
                                lore.set(i, InventoryHelper.replaceString("shop", lore.get(i), purchasedItemKey));
                            }
                            purchasedItemMeta.setLore(lore);
                            purchasedItem.setItemMeta(purchasedItemMeta);
                            break;
                        }
                    }

                    // Check if the player wants to buy an item
                    if (CustomConfigs.get("buyMenu").getString("items." + key + ".itemData.type").equals("BUY")) {

                        // Check if the player has enough money
                        if (Main.econ.getBalance(player) >= (itemPrice * calculateAmount)) {

                            // Check if the player has enough space in their inventory
                            if (!InventoryHelper.canAddItem(player, purchasedItem)) {
                                player.sendMessage("§cYou don't have enough space in your inventory to buy this item.");
                                event.setCancelled(true);
                                break;
                            }

                            player.getInventory().addItem(purchasedItem);

                            Main.econ.withdrawPlayer(player, (itemPrice * calculateAmount));
                            break;
                        } else {
                            player.sendMessage("§cYou don't have enough money to buy this item. (You need " + (itemPrice * calculateAmount) + ")");
                            event.setCancelled(true);
                            break;
                        }

                    // Check if the player wants to sell an item
                    }else if (CustomConfigs.get("buyMenu").getString("items." + key + ".itemData.type").equals("SELL")) {

                        // Check if the player has enough of the item to sell
                        int buyAmount = CustomConfigs.get("buyMenu").getInt("items." + key + ".itemData.amount");

                        if (Main.getItemAmount(player, purchasedItem) >= buyAmount) {

                            player.getInventory().removeItem(purchasedItem);

                            Main.econ.depositPlayer(player, (itemSellPrice * calculateAmount));

                            player.sendMessage("§aYour new balance is: " + Main.econ.getBalance(player));

                        }else {
                            player.sendMessage("§cYou don't have enough of this item to sell.");
                        }
                    }
                }
            }
            event.setCancelled(true);
        }
    }
}
